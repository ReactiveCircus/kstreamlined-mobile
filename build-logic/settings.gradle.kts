enableFeaturePreview("NO_IMPLICIT_LOOKUP_IN_PARENT_PROJECTS")

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

buildCache {
    remote<HttpBuildCache> {
        val buildCacheUrl = envOrProp(("KS_REMOTE_BUILD_CACHE_URL"))
        isEnabled = buildCacheUrl.isPresent
        if (buildCacheUrl.isPresent) {
            url = uri(buildCacheUrl.get())
            credentials {
                username = envOrProp("KS_REMOTE_BUILD_CACHE_USERNAME").orNull
                password = envOrProp("KS_REMOTE_BUILD_CACHE_PASSWORD").orNull
            }
            isPush = System.getenv("CI") == "true" && System.getenv("GITHUB_REF") == "refs/heads/main"
        }
    }
}

private fun Settings.envOrProp(name: String): Provider<String> =
    providers.environmentVariable(name).orElse(providers.gradleProperty(name))
