plugins {
    id("maven-publish")
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    @Suppress("UnstableApiUsage")
    configureBasedOnAppliedPlugins(
        sourcesJar = true,
        javadocJar = true
    )
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()

    coordinates(
        groupId = Config.Publishing.GROUP_ID,
        version = Config.Version.NAME
    )

    pom {
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