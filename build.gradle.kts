plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.fusedlibrary) apply false
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    group = Config.GROUP_ID

    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(System.getenv("MCR_USERNAME"))
            password.set(System.getenv("MCR_PASSWORD"))
        }
    }
}