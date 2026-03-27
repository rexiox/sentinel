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

    val compileNativeSecurity by tasks.creating(Exec::class) {
        val scriptFile = project.file("src/nativeInterop/cinterop/build.sh")
        workingDir = scriptFile.parentFile
        commandLine("sh", scriptFile.name)
    }

    iosTargets.forEach { target ->
        target.binaries.framework {
            baseName = "sentinel-kit-detector"
        }

        target.compilations.getByName("main") {
            cinterops {
                val libSubDir =
                    if (target.konanTarget.name.contains("simulator") ||
                        target.konanTarget.name.contains("x64")
                    ) {
                        "sim"
                    } else {
                        "device"
                    }

                val detector by creating {
                    definitionFile.set(project.file("src/nativeInterop/cinterop/def/detector_$libSubDir.def"))

                    includeDirs.allHeaders(
                        includeDirs = listOf(
                            "src/nativeInterop/cinterop/debugger/",
                            "src/nativeInterop/cinterop/hook/"
                        )
                    )
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":sentinel-core"))
            }
        }

        androidMain {
            dependencies {
                api(project(":sentinel-kit:ndk"))
            }
        }
    }
}