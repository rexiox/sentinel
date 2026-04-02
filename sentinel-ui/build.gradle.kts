import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("sentinel-publish")
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

compose.resources {
    publicResClass = false
    packageOfResClass = "com.rs.sentinel.ui.resources"
    generateResClass = auto
}

kotlin {

    android {
        namespace = "${Config.NAMESPACE}.ui"

        compileSdk {
            version = release(36) { minorApiLevel = 1 }
        }

        minSdk = 24

        androidResources.enable = true
    }

    val xcfName = "sentinel-ui"

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
        commonMain {
            dependencies {
                api(project(":sentinel"))

                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(compose.components.resources)
                implementation(libs.kotlin.reflect)
                implementation(libs.material.icons.core)
            }
        }
    }

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}