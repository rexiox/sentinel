plugins {
    alias(libs.plugins.android.fusedlibrary)
    `maven-publish`
}

group = Config.GROUP_ID
version = Config.Version.NAME

androidFusedLibrary {
    namespace = Config.NAMESPACE

    minSdk {
        version = release(Config.Version.MIN_SDK)
    }
}

dependencies {

    include(project(":sentinel"))
    include(project(":core"))
    include(project(":kit:root"))
    include(project(":kit:tamper"))
    include(project(":kit:emulator"))
    include(project(":kit:debug"))
    include(project(":kit:hook"))
    include(project(":kit:location"))
}

publishing {
    publications {
        create<MavenPublication>("release") {
            artifactId = Config.Publishing.ARTIFACT_ID

            afterEvaluate {
                from(components["fusedLibraryComponent"])
            }

            pom {
                name.set(Config.Publishing.NAME)
                description.set(Config.Publishing.DESCRIPTION)
                url.set(Config.Publishing.URL)

                licenses {
                    license {
                        name.set(Config.Publishing.License.NAME)
                        url.set(Config.Publishing.License.URL)
                    }
                }

                developers {
                    developer {
                        id.set(Config.Publishing.Developer.ID)
                        name.set(Config.Publishing.Developer.NAME)
                        email.set(Config.Publishing.Developer.EMAIL)
                    }
                }

                scm {
                    connection.set(Config.Publishing.SCM.CONNECTION)
                    developerConnection.set(Config.Publishing.SCM.DEV_CONNECTION)
                    url.set(Config.Publishing.SCM.URL)
                }
            }
        }
    }
}

/*
afterEvaluate {
    signing {
        useInMemoryPgpKeys(
            System.getenv("PGP_KEY"),
            System.getenv("PGP_KEY_PASSWORD")
        )
        sign(publishing.publications["release"])
    }
}
*/