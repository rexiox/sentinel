pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://androidx.dev/kmp/builds/10385446/artifacts/snapshots/repository")
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/kmp/builds/10385446/artifacts/snapshots/repository")
        }
    }
}

rootProject.name = "sentinel"

include(":sentinel")
include(":sentinel-ui")
include(":sentinel-core")
include(":sentinel-monitor")
include(":sentinel-runtime")
include(":sentinel-identity")
include(":sentinel-benchmark")
include(":sentinel-kit")
include(":sentinel-kit:kni")
include(":sentinel-kit:ndk")
include(":sentinel-kit:detector")

include(":sample")
include(":sample:android")
include(":sample:multiplatform")
include(":sample:multiplatform:composeApp")
