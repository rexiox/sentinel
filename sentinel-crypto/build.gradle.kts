plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.sentinel.publish)
    alias(libs.plugins.kotlinx.serialization)
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {

    android {
        namespace = "${Config.NAMESPACE}.crypto"

        compileSdk {
            version = release(36) { minorApiLevel = 1 }
        }

        minSdk = 24
    }

    val xcfName = "sentinel-crypto"

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
            dependencies { }
        }

        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }


    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}