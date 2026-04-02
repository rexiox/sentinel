plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    id("sentinel-publish")
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {
    android {
        namespace = "${Config.NAMESPACE}.kit.detector"

        compileSdk {
            version = release(36) { minorApiLevel = 1 }
        }

        minSdk = 24
    }

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    iosTargets.forEach { target ->
        target.binaries.framework {
            baseName = "sentinel-kit-detector"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":sentinel-core"))
                api(project(":sentinel-runtime"))
            }
        }

        androidMain {
            dependencies {
                api(project(":sentinel-kit:ndk"))
            }
        }

        iosMain {
            dependencies {
                api(project(":sentinel-kit:kni"))
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}