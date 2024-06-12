pluginManagement {
    repositories {
        gradlePluginPortal {
            content {
                includeGroupByRegex("org.gradle.*")
            }
        }
        mavenCentral()
    }

    val gradleToolchainsResolverVersion = file("../gradle/libs.versions.toml")
        .readLines()
        .first { it.contains("gradle-toolchainsResolverPlugin") }
        .substringAfter("=")
        .trim()
        .removeSurrounding("\"")

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version gradleToolchainsResolverVersion
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("com.google.*")
            }
        }
        // TODO remove once released
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots") {
            content {
                includeGroup("com.squareup.invert")
            }
        }
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}
