import com.android.build.api.dsl.SettingsExtension

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
                includeGroupByRegex("com.gradle.*")
                includeGroupByRegex("org.gradle.*")
            }
        }
        mavenCentral()
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
    }
}

plugins {
    id("com.gradle.develocity")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.android.settings")
}

rootProject.name = "kstreamlined-mobile"

includeKmpProjects(
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
)

includeAndroidProjects(
    ":app",
    ":benchmark",
    ":feature:common",
    ":feature:content-viewer",
    ":feature:home",
    ":feature:kotlin-weekly-issue",
    ":feature:saved-for-later",
    ":feature:settings",
    ":feature:talking-kotlin-episode",
    ":foundation:common-ui:feed",
    ":foundation:designsystem",
    ":foundation:compose-utils",
    ":foundation:screenshot-testing:tester",
    ":foundation:screenshot-testing:paparazzi",
    ":foundation:scheduled-work",
)

private fun includeKmpProjects(vararg projectPaths: String) {
    include(*projectPaths)
}

private fun includeAndroidProjects(vararg projectPaths: String) {
    if (startParameter.taskNames.any { it.endsWith("XCFramework") }) return
    for (path in projectPaths) {
        includeProject(path, "android/${path.replace(":", "/")}")
    }
}

private fun includeProject(name: String, filePath: String) {
    include(name)
    val project = project(name)
    project.projectDir = file(filePath)
    var current = project
    while (current.parent != null && current.parent != rootProject) {
        val parent = current.parent!!
        val defaultDir = File(parent.parent!!.projectDir, parent.name)
        if (parent.projectDir == defaultDir) {
            parent.projectDir = current.projectDir.parentFile
        }
        current = parent
    }
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

settings.extensions.configure<SettingsExtension> {
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
