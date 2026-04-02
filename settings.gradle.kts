pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "sentinel"

include(":sentinel")
include(":sentinel-ui")
include(":sentinel-core")
include(":sentinel-runtime")
include(":sentinel-identity")
include(":sentinel-kit")
include(":sentinel-kit:kni")
include(":sentinel-kit:ndk")
include(":sentinel-kit:detector")

include(":sample")
include(":sample:android")
include(":sample:multiplatform")
include(":sample:multiplatform:composeApp")
