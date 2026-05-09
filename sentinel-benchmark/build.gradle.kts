@file:OptIn(
    ExperimentalWasmDsl::class,
    ExperimentalKotlinGradlePluginApi::class
)

import com.android.build.gradle.internal.tasks.ManagedDeviceTestTask
import kotlinx.benchmark.gradle.benchmark
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    kotlin("plugin.allopen") version "2.3.20"
    id("com.android.library")
    id("org.jetbrains.kotlinx.benchmark") version "0.4.17"
    id("androidx.benchmark") version "1.4.1" apply false
    id("androidx.benchmark.darwin")
    id("performance-report")
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

kotlin {
    val xcf = XCFramework("AndroidXDarwinBenchmarks")

    androidTarget("android") {
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

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

        val androidInstrumentedTest by getting {
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
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR,LOW-BATTERY"
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

    testOptions {
        @Suppress("UnstableApiUsage")
        managedDevices {
            localDevices {
                create("pixel2api30") {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp"
                }

                create("nexus5api27") {
                    device = "Nexus 5"
                    apiLevel = 27
                    systemImageSource = "aosp"
                }

                create("pixelCapi30") {
                    device = "Pixel C"
                    apiLevel = 30
                    systemImageSource = "aosp"
                }
            }

            groups {
                create("phoneAndTablet") {
                    targetDevices.add(allDevices["pixel2api30"])
                    targetDevices.add(allDevices["nexus5api27"])
                    targetDevices.add(allDevices["pixelCapi30"])
                }
            }
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

darwinBenchmark {
    xcodeProjectName.set("AndroidXDarwinBenchmarks")
    scheme.set("AndroidXDarwinBenchmarks")
}

tasks.withType<ManagedDeviceTestTask>().configureEach {
    finalizedBy("generateAndroidBenchmarkPerformanceReport")
}

tasks.register("generatePerformanceReport") {
    group = "reporting"
    dependsOn("generateAndroidBenchmarkPerformanceReport")
}