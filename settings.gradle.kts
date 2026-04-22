pluginManagement {
    includeBuild("build-logic")
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
                includeGroupByRegex("com.gradle.*")
                includeGroupByRegex("org.gradle.*")
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

    fun extractVersionFromCatalog(key: String) = file("$rootDir/gradle/libs.versions.toml")
        .readLines()
        .first { it.contains(key) }
        .substringAfter("=")
        .trim()
        .removeSurrounding("\"")

    plugins {
        id("com.gradle.develocity") version extractVersionFromCatalog("gradle-develocityPlugin")
        id("org.gradle.toolchains.foojay-resolver-convention") version extractVersionFromCatalog("gradle-toolchainsResolverPlugin")
        id("com.android.settings") version extractVersionFromCatalog("androidGradlePlugin")
    }
}

includeBuild("build-logic")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
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
}

plugins {
    id("com.gradle.develocity")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.android.settings")
    id("kstreamlined.settings")
}

rootProject.name = "kstreamlined-mobile"

includeKmpProjects(
    ":kmp:app-info",
    ":kmp:feed-datasource",
    ":kmp:feed-model",
    ":kmp:feed-sync:common",
    ":kmp:feed-sync:runtime",
    ":kmp:feed-sync:testing",
    ":kmp:settings-datasource",
    ":kmp:settings-model",
    ":kmp:remote:common",
    ":kmp:remote:cloud",
    ":kmp:remote:edge",
    ":kmp:remote:mock",
    ":kmp:remote:testing",
    ":kmp:database",
    ":kmp:database-testing",
    ":kmp:datastore-testing",
    ":kmp:network-monitor:common",
    ":kmp:network-monitor:runtime",
    ":kmp:network-monitor:testing",
    ":kmp:presentation:common",
    ":kmp:presentation:content-viewer",
    ":kmp:presentation:home",
    ":kmp:presentation:kotlin-weekly-issue",
    ":kmp:presentation:saved-for-later",
    ":kmp:presentation:settings",
    ":kmp:presentation:talking-kotlin-episode",
    ":kmp:pretty-time",
    ":kmp:pulse:metro",
    ":kmp:pulse:runtime",
)

includeAndroidProjects(
    ":app",
    ":benchmark",
    ":feature:content-viewer:api",
    ":feature:content-viewer:impl",
    ":feature:home",
    ":feature:kotlin-weekly-issue:api",
    ":feature:kotlin-weekly-issue:impl",
    ":feature:licenses:api",
    ":feature:licenses:impl",
    ":feature:saved-for-later",
    ":feature:settings:api",
    ":feature:settings:impl",
    ":feature:talking-kotlin-episode:api",
    ":feature:talking-kotlin-episode:impl",
    ":core:designsystem",
    ":core:launcher",
    ":core:scheduled-work",
    ":core:screenshot-testing:tester",
    ":core:screenshot-testing:paparazzi",
    ":core:ui:feed",
    ":core:ui:pattern",
    ":core:ui:util",
)

private fun includeKmpProjects(vararg projectPaths: String) {
    include(*projectPaths)
}

private fun includeAndroidProjects(vararg projectPaths: String) {
    if (startParameter.taskNames.any { it.endsWith("XCFramework") }) return
    include(*projectPaths)
    // redirect all project directories (including intermediate parents) under `android/`
    projectPaths
        .flatMap { path ->
            val segments = path.removePrefix(":").split(":")
            (1..segments.size).map { i -> ":" + segments.subList(0, i).joinToString(":") }
        }
        .forEach { project(it).projectDir = file("android${it.replace(":", "/")}") }
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
        publishing.onlyIf {
            System.getenv("CI") == "true"
        }
    }
}

android {
    execution {
        profiles {
            create("local") {
                r8 {
                    jvmOptions += "-Xms8g -Xmx8g -XX:MetaspaceSize=1g -XX:SoftRefLRUPolicyMSPerMB=1 -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -XX:+UnlockExperimentalVMOptions".split(" ")
                    runInSeparateProcess = true
                }
            }
            create("ci") {
                r8 {
                    jvmOptions += "-Xms4g -Xmx4g -XX:MetaspaceSize=1g -XX:SoftRefLRUPolicyMSPerMB=1 -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -XX:+UnlockExperimentalVMOptions".split(" ")
                    runInSeparateProcess = true
                }
            }
            defaultProfile = "local"
        }
    }
}
