plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.sentinel.publish)
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {

    android {
        namespace = "${Config.NAMESPACE}.kit.kni"

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
            baseName = "sentinel-kit-kni"
        }

        target.compilations.getByName("main") {
            cinterops {
                val src = "src/nativeInterop/cinterop"

                val libSubDir =
                    if (target.konanTarget.name.contains("simulator") ||
                        target.konanTarget.name.contains("x64")
                    ) {
                        "sim"
                    } else {
                        "device"
                    }

                @Suppress("unused")
                val detector by creating {
                    definitionFile.set(project.file("$src/def/detector_$libSubDir.def"))

                    includeDirs.allHeaders(
                        includeDirs = listOf(
                            "$src/detector/jailbreak/",
                            "$src/detector/tamper/",
                            "$src/detector/hook/",
                            "$src/detector/debugger/"
                        )
                    )
                }
            }
        }
    }
}