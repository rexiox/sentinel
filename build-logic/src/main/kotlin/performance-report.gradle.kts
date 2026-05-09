import groovy.json.JsonSlurper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.sqrt

tasks.register("generateAndroidBenchmarkPerformanceReport") {
    group = "reporting"
    description = "Analyses JSON benchmark results and generates a Markdown performance report."

    val version = "v1.8.4.beta"
    val projectReportDir = project.layout.projectDirectory.dir("report/performance/${version.replace(".", "_")}/").asFile
    val benchmarkDir = project.layout.buildDirectory.dir("outputs/connected_android_test_additional_output/releaseAndroidTest/connected")

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
        md.append("> **Version:** $version\n\n")

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

        val deviceName = build?.let {
            "${it["brand"]}_${it["model"]}"
        } ?: "unknown_device"

        val safeDeviceName = deviceName
            .replace("\\s+".toRegex(), "_")
            .replace("[^A-Za-z0-9_]".toRegex(), "")

        val categories = listOf(
            "Runtime Performance (Inspect Only)" to { name: String ->
                name.startsWith("benchmarkInspect") && !name.contains("Warmup")
            },
            "Cold Start Performance" to { name: String ->
                name.startsWith("benchmarkColdStart")
            },
            "Initialization Only" to { name: String ->
                name.startsWith("benchmarkInitialization")
            },
            "Memory Allocation Tests" to { name: String ->
                name.startsWith("benchmarkMemory")
            },
            "Instance Reuse Tests" to { name: String ->
                name.startsWith("benchmarkReuse")
            },
            "Edge Cases" to { name: String ->
                name.contains("Empty") || name.contains("Concurrent")
            },
            "Manual Warmup Tests" to { name: String ->
                name.contains("Warmup")
            }
        )

        categories.forEach { (title, filterFn) ->
            val tests = benchmarks.filter {
                val fullName = it["name"] as String
                val simpleName = fullName.substringAfterLast(".")
                filterFn(simpleName)
            }

            if (tests.isEmpty()) return@forEach

            md.append("\n### $title\n\n")
            md.append("| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |\n")
            md.append("| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |\n")

            tests.forEach { benchmark ->
                val fullName = benchmark["name"] as String
                val simpleName = fullName.substringAfterLast(".")
                    .replace("benchmark", "")
                    .replace("Inspect", "")
                    .replace("ColdStart", "CS_")
                    .replace("Initialization", "Init_")
                    .replace("Memory", "Mem_")

                val metrics = benchmark["metrics"] as? Map<*, *> ?: return@forEach

                metrics.forEach { (metricName, values) ->
                    val stats = values as Map<*, *>
                    val isTime = metricName == "timeNs"
                    val factor = if (isTime) 1_000_000.0 else 1.0

                    val min = (stats["minimum"] as Number).toDouble() / factor
                    val median = (stats["median"] as Number).toDouble() / factor
                    val max = (stats["maximum"] as Number).toDouble() / factor
                    val runs = (stats["runs"] as? List<*>)?.size ?: "N/A"

                    val runsList = (stats["runs"] as? List<*>)?.map {
                        (it as Number).toDouble() / factor
                    }?.sorted() ?: emptyList()

                    val p95 = if (runsList.isNotEmpty()) {
                        val idx = (runsList.size * 0.95).toInt().coerceAtMost(runsList.size - 1)
                        runsList[idx]
                    } else {
                        median
                    }

                    val metricLabel = when (metricName) {
                        "timeNs" -> "Latency (ms)"
                        else -> "Alloc (count)"
                    }

                    val indicator = if (metricName == "timeNs") {
                        when {
                            median < 20 -> "🟢"
                            median < 50 -> "🟡"
                            median < 100 -> "🟠"
                            else -> "🔴"
                        }
                    } else {
                        ""
                    }

                    val bar = if (metricName == "timeNs") {
                        when {
                            median < 1 -> "█"
                            median < 5 -> "██"
                            median < 10 -> "███"
                            median < 20 -> "████"
                            median < 50 -> "██████"
                            median < 100 -> "████████"
                            else -> "██████████"
                        }
                    } else {
                        val normalizedAlloc = (median / 100.0).coerceIn(1.0, 10.0).toInt()
                        "█".repeat(normalizedAlloc)
                    }

                    md.append("| $simpleName | $metricLabel | $runs |")
                    md.append(
                        "${String.format("%.2f", min)} | **${
                            String.format(
                                "%.2f",
                                median
                            )
                        }** | "
                    )
                    md.append("${String.format("%.2f", max)} | ")
                    md.append("${String.format("%.2f", p95)} | ")
                    md.append("$indicator | `$bar` |\n")
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

            val sorted = latencies.sorted()
            val p50 = sorted[sorted.size / 2]
            val p95 = sorted[(sorted.size * 0.95).toInt().coerceAtMost(sorted.size - 1)]
            val p99 = sorted[(sorted.size * 0.99).toInt().coerceAtMost(sorted.size - 1)]

            md.append("\n## Statistical Analysis\n\n")
            md.append("| Metric | Value |\n")
            md.append("| :--- | :--- |\n")
            md.append("| **Total Tests** | ${latencies.size} |\n")
            md.append("| **Average Latency** | ${String.format("%.2f", avg)} ms |\n")
            md.append("| **Median (P50)** | ${String.format("%.2f", p50)} ms |\n")
            md.append("| **P95 Latency** | ${String.format("%.2f", p95)} ms |\n")
            md.append("| **P99 Latency** | ${String.format("%.2f", p99)} ms |\n")
            md.append("| **Fastest Test** | ${String.format("%.2f", latencies.minOrNull())} ms |\n")
            md.append("| **Slowest Test** | ${String.format("%.2f", latencies.maxOrNull())} ms |\n")
            md.append("| **Std Deviation** | ${String.format("%.2f", stdDev)} ms |\n")
            md.append("| **Coeff. of Variation** | ${String.format("%.2f", cv)}% |\n")

            val excellent = latencies.count { it < 20 }
            val good = latencies.count { it in 20.0..49.9 }
            val acceptable = latencies.count { it in 50.0..99.9 }
            val critical = latencies.count { it >= 100 }

            md.append("\n### Performance Distribution\n\n")
            md.append("| Status | Count | Percentage |\n")
            md.append("| :--- | :---: | :---: |\n")
            md.append(
                "| 🟢 Excellent (< 20ms) | $excellent | ${
                    String.format(
                        "%.1f",
                        excellent * 100.0 / latencies.size
                    )
                }% |\n"
            )
            md.append(
                "| 🟡 Good (20-50ms) | $good | ${
                    String.format(
                        "%.1f",
                        good * 100.0 / latencies.size
                    )
                }% |\n"
            )
            md.append(
                "| 🟠 Acceptable (50-100ms) | $acceptable | ${
                    String.format(
                        "%.1f",
                        acceptable * 100.0 / latencies.size
                    )
                }% |\n"
            )
            md.append(
                "| 🔴 Critical (> 100ms) | $critical | ${
                    String.format(
                        "%.1f",
                        critical * 100.0 / latencies.size
                    )
                }% |\n"
            )
        }

        md.append("\n## Performance Evaluation Reference\n\n")
        md.append("Visual indicators are based on Android performance best practices and human perception thresholds.\n\n")
        md.append("| Status | Indicator | Detection Time | Basis | Description |\n")
        md.append("| :--- | :--- | :--- | :--- | :--- |\n")
        md.append("| **Excellent** | 🟢 | < 20ms | **Imperceptible** | Near-instant detection with zero user impact. |\n")
        md.append("| **Good** | 🟡 | 20ms – 50ms | **Fast Enough** | Quick validation suitable for production use. |\n")
        md.append("| **Acceptable** | 🟠 | 50ms – 100ms | **Perception Threshold** | Noticeable but tolerable delay. |\n")
        md.append("| **Critical** | 🔴 | > 100ms | **Human Perception** | Delay becomes disruptive to user experience. |\n\n")

        md.append("\n## Notes\n\n")
        md.append("- **Inspect Only**: Tests measure runtime performance of already-initialized instances\n")
        md.append("- **Cold Start**: Tests include both initialization and first inspection\n")
        md.append("- **Initialization Only**: Tests measure only the setup/configuration overhead\n")
        md.append("- **P95**: 95% of all measurements are below this value (excludes outliers)\n")
        md.append("- **CV**: Coefficient of Variation - lower is better (< 20% is excellent)\n\n")

        val fileName = "${fileTimestamp}_${safeDeviceName}_android.md"
        val reportFile = File(projectReportDir, fileName)

        if (!projectReportDir.exists()) projectReportDir.mkdirs()
        reportFile.writeText(md.toString())

        println("Report generated: ${reportFile.absolutePath}")
    }
}