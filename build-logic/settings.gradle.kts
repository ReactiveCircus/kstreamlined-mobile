pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("com.google.*")
            }
        }
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
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            content {
                // TODO remove once upgraded Paparazzi to 2.0.0-alpha05
                includeGroup("app.cash.paparazzi")
            }
        }
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

rootProject.name = "build-logic"
include(":kstreamlined-build-plugin")
include(":kstreamlined-settings-plugin")
include(":aab-publisher")
include(":chameleon:chameleon-compiler-plugin")
include(":chameleon:chameleon-gradle-plugin")
include(":chameleon:chameleon-runtime")
include(":licentia:licentia-gradle-plugin")
include(":licentia:licentia-runtime")
include(":routebinding:routebinding-compiler-plugin")
include(":routebinding:routebinding-gradle-plugin")
include(":routebinding:routebinding-runtime")
include(":v2p")
