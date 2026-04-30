@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import kotlinx.benchmark.gradle.benchmark

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.allopen") version "2.3.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.16"
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

kotlin {
    androidTarget("android") {}

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.benchmark.runtime)
                implementation(project(":sentinel"))
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

        val iosArm64Benchmark by creating {
            dependsOn(commonBenchmark)
        }
    }
}

android {
    namespace = "com.rexiox.sentinel.benchmark"
    compileSdk = 36

    testBuildType = "release"

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] =
            "DEBUGGABLE,EMULATOR,LOW-BATTERY"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

        create("mobile") {
            iterations = 3
            iterationTime = 200
            iterationTimeUnit = "ms"
        }
    }

    targets {
        register("android")
        register("iosArm64")
        register("iosSimulatorArm64")
        register("iosX64")
    }
}