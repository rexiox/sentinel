import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sentinel.publish)
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

compose.resources {
    publicResClass = true
    packageOfResClass = "${Config.NAMESPACE}.monitor.resources"
    generateResClass = always
}

kotlin {

    android {
        namespace = "${Config.NAMESPACE}.monitor"

        compileSdk {
            version = release(36) { minorApiLevel = 1 }
        }

        minSdk = 24

        androidResources.enable = true
    }

    val xcfName = "sentinel-monitor"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {

        androidMain {
            dependencies {
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.activity.compose)
            }
        }

        commonMain {
            dependencies {
                implementation(project(":sentinel"))
                implementation(project(":sentinel-ui"))

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.compose.components.resources)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.material.icons.core)
                implementation(libs.kotlinx.datetime)
            }
        }
    }

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}