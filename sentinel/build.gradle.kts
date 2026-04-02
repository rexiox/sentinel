import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("sentinel-publish")
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {
    android {
        namespace = Config.NAMESPACE
        compileSdk = Config.Version.COMPILE_SDK
        minSdk = Config.Version.MIN_SDK

        withJava()

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_11
                )
            }
        }

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-proguard-rules.pro")
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":sentinel-core"))
            api(project(":sentinel-runtime"))
            api(project(":sentinel-identity"))
            api(project(":sentinel-kit:detector"))

            implementation(libs.kotlinx.coroutines.core)
        }
    }

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}