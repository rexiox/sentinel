import kotlinx.validation.ExperimentalBCVApi

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.fusedlibrary) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlinx.binary.compatibility.validator)
    alias(libs.plugins.androidx.benchmark.darwin) apply false
}

apiValidation {
    ignoredProjects.addAll(
        elements = listOf(
            "sentinel-benchmark",
            "sample",
            "composeApp",
            "android"
        )
    )

    @OptIn(ExperimentalBCVApi::class)
    klib {
        enabled = true
    }
}