plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.sentinel.publish)
}

android {
    namespace = "${Config.NAMESPACE}.kit.ndk"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        @Suppress("UnstableApiUsage")
        externalNativeBuild {
            cmake {
                abiFilters("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            }
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}