@file:OptIn(ExperimentalWasmDsl::class)

import groovy.json.JsonSlurper
import kotlinx.benchmark.gradle.benchmark
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.sqrt

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.allopen") version "2.3.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.16"
    id("androidx.benchmark") version "1.4.1" apply false
    id("androidx.benchmark.darwin")
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

kotlin {
    val xcf = XCFramework("AndroidXDarwinBenchmarks")

    androidTarget("android") {}

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "AndroidXDarwinBenchmarks"
            xcf.add(this)
            export("androidx.benchmark:benchmark-darwin:1.2.0-SNAPSHOT")
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.benchmark.runtime)
                implementation(project(":sentinel"))
                api("androidx.benchmark:benchmark-darwin:1.2.0-SNAPSHOT")
            }
        }

        val commonBenchmark by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(commonBenchmark)
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.junit.ktx)
                implementation(libs.androidx.benchmark.junit4)
            }
        }

        val iosMain by getting {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.rexiox.sentinel.benchmark"
    compileSdk = 36

    testBuildType = "release"

    defaultConfig {
        @Suppress("DEPRECATION")
        targetSdk = 36
        minSdk = 24

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.report.enable"] = "true"
        testInstrumentationRunnerArguments["androidx.benchmark.profiling.mode"] = "none"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "LOW-BATTERY"
        testInstrumentationRunnerArguments["androidx.benchmark.outputFormat"] = "JSON"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

benchmark {
    configurations {
        named("main") {
            iterations = 5
            warmups = 3
            iterationTime = 500
            iterationTimeUnit = "ms"
            outputTimeUnit = "ns"
        }
    }

    targets {
        register("iosArm64")
        register("iosX64")
        register("iosSimulatorArm64")
    }
}

tasks.register("generateAndroidBenchmarkPerformanceReport") {
    group = "reporting"
    description = "Analyses JSON benchmark results and generates a Markdown performance report."

    val projectReportDir = project.layout.projectDirectory.dir("report/performance").asFile
    val benchmarkDir = project.layout.buildDirectory
        .dir("outputs/connected_android_test_additional_output/releaseAndroidTest/connected")

    inputs.dir(benchmarkDir).withPropertyName("benchmarkDir").optional()

    doLast {
        val jsonFile = benchmarkDir.get().asFile.walkTopDown()
            .filter { it.extension == "json" && "benchmark" in it.name }
            .maxByOrNull { it.lastModified() }
            ?: return@doLast

        val data = JsonSlurper().parse(jsonFile) as Map<*, *>
        val context = data["context"] as? Map<*, *>
        val build = context?.get("build") as? Map<*, *>
        val benchmarks = (data["benchmarks"] as? List<*>)
            ?.filterIsInstance<Map<*, *>>() ?: emptyList()

        val now = LocalDateTime.now()
        val fileTimestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
        val displayDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val md = StringBuilder()

        md.append("# Benchmark Performance Report\n\n")
        md.append("> **Generated on:** $displayDate\n\n")

        md.append("## Device Information\n\n")
        md.append("| Property | Value |\n")
        md.append("| :--- | :--- |\n")
        build?.run {
            val versionMap = this["version"] as? Map<*, *>
            val sdkVersion = versionMap?.get("sdk") ?: context["sdk"] ?: "N/A"
            val cpuFreq = (context["cpuMaxFreqHz"] as? Number)?.toLong()?.div(1_000_000) ?: 0
            val memGB = String.format(
                "%.1f",
                ((context["memTotalBytes"] as? Number)?.toLong() ?: 0) / 1_073_741_824.0
            )

            md.append("| **Device** | ${this["brand"]} ${this["model"]} |\n")
            md.append("| **Android** | API $sdkVersion |\n")
            md.append("| **CPU** | ${context["cpuCoreCount"]} cores @ $cpuFreq MHz |\n")
            md.append("| **Memory** | $memGB GB RAM |\n")
        }
        md.append("\n---\n")

        val categories = listOf(
            "Memory Allocation" to "Memory",
            "Cold Start Performance" to "ColdStart",
            "Individual Module Performance" to "Only",
            "Warmed Up Performance" to "Warmup",
            "All Modules Combined" to "AllModules",
            "Sequential Execution" to "Sequential"
        )

        categories.forEach { (title, filter) ->
            val tests =
                benchmarks.filter { (it["name"] as String).contains(filter, ignoreCase = true) }
            if (tests.isEmpty()) return@forEach

            md.append("\n### $title\n\n")
            md.append("| Test Case | Metric  | Run | Min | Median | Max | Status | Visual |\n")
            md.append("| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |\n")

            tests.forEach { benchmark ->
                val simpleName = (benchmark["name"] as String).substringAfterLast(".")
                    .replace("benchmark", "").replace("Inspect", "")
                val metrics = benchmark["metrics"] as? Map<*, *> ?: return@forEach

                metrics.forEach { (metricName, values) ->
                    val stats = values as Map<*, *>
                    val isTime = metricName == "timeNs"
                    val factor = if (isTime) 1_000_000.0 else 1.0

                    val min = (stats["minimum"] as Number).toDouble() / factor
                    val median = (stats["median"] as Number).toDouble() / factor
                    val max = (stats["maximum"] as Number).toDouble() / factor
                    val runs = (stats["runs"] as? List<*>)?.size ?: "N/A"

                    val metricLabel = when (metricName) {
                        "timeNs" -> "Latency (ms)"
                        else -> "Alloc (count)"
                    }

                    val indicator = when {
                        median < 30 -> "🟢"
                        median < 70 -> "🟡"
                        median < 120 -> "🟠"
                        else -> "🔴"
                    }

                    val status = if (metricName == "timeNs") {
                        indicator
                    } else {
                        ""
                    }

                    val bar =
                        "█".repeat((median.coerceIn(1.0, 100.0) / 10).toInt().coerceAtLeast(1))

                    md.append("| $simpleName | $metricLabel | $runs |")
                    md.append(
                        "${String.format("%.2f", min)} | **${
                            String.format(
                                "%.2f",
                                median
                            )
                        }** | "
                    )
                    md.append("${String.format("%.2f", max)} | $status | `$bar` |\n")
                }
            }
        }

        val latencies = benchmarks.mapNotNull {
            ((it["metrics"] as? Map<*, *>)?.get("timeNs") as? Map<*, *>)?.get("median") as? Number
        }.map { it.toDouble() / 1_000_000 }

        if (latencies.isNotEmpty()) {
            val avg = latencies.average()
            val stdDev = sqrt(latencies.map { (it - avg).pow(2) }.average())
            val cv = (stdDev / avg) * 100

            md.append("\n## Statistical Analysis\n\n")
            md.append("| Metric | Value |\n")
            md.append("| :--- | :--- |\n")
            md.append("| **Average Latency** | ${String.format("%.2f", avg)} ms |\n")
            md.append("| **Fastest Test** | ${String.format("%.2f", latencies.minOrNull())} ms |\n")
            md.append("| **Slowest Test** | ${String.format("%.2f", latencies.maxOrNull())} ms |\n")
            md.append("| **Coeff. of Variation** | ${String.format("%.2f", cv)}% |\n")
        }

        md.append("\n# Performance Evaluation Reference\n")
        md.append("Visual indicators are based on RAIL performance model and human perception latency thresholds.\n\n")
        md.append("| Status | Indicator | Detection Time | Basis | Description |\n")
        md.append("| :--- | :--- | :--- | :--- | :--- |\n")
        md.append("| **Excellent** | 🟢 | < 30ms | **Instant Response** | Detection completes instantly with no startup impact. |\n")
        md.append("| **Good** | 🟡 | 30ms – 70ms | **RAIL Model** | Fast security validation suitable for production. |\n")
        md.append("| **Acceptable** | 🟠 | 70ms – 120ms | **Perception Threshold** | Slight delay measurable but not disturbing. |\n")
        md.append("| **Critical** | 🔴 | > 120ms | **Human Perception** | Startup latency becomes noticeable. |\n\n")

        val fileName = "${fileTimestamp}_android.md"
        val reportFile = File(projectReportDir, fileName)

        if (!projectReportDir.exists()) projectReportDir.mkdirs()
        reportFile.writeText(md.toString())
    }
}