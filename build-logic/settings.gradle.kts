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
include(":chameleon:chameleon-compiler-plugin")
include(":chameleon:chameleon-gradle-plugin")
include(":chameleon:chameleon-runtime")
include(":licentia:licentia-gradle-plugin")
include(":licentia:licentia-runtime")
include(":v2p")
