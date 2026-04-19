import dev.mokkery.gradle.MokkeryGradleExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.dev.mokkery)
    alias(libs.plugins.sentinel.publish)
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {
    androidTarget {
        publishLibraryVariants()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "sentinel-kit-detector"
            isStatic = true
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

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        iosTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidUnitTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.roboelectric)
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.core)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "${Config.NAMESPACE}.kit.detector"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = true
    }
}

extensions.configure<MokkeryGradleExtension> {
    stubs.allowConcreteClassInstantiation.set(true)
    stubs.allowClassInheritance.set(true)
}