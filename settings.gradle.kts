import com.android.build.api.dsl.SettingsExtension

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
    }

    fun extractVersionFromCatalog(key: String) = file("$rootDir/gradle/libs.versions.toml")
        .readLines()
        .first { it.contains(key) }
        .substringAfter("=")
        .trim()
        .removeSurrounding("\"")

    plugins {
        id("com.gradle.enterprise") version extractVersionFromCatalog("gradle-enterprise")
        id("org.gradle.toolchains.foojay-resolver-convention") version extractVersionFromCatalog("gradle-toolchainsResolverPlugin")
        id("com.android.settings") version extractVersionFromCatalog("androidGradlePlugin")
    }
}

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
        maven {
            url = uri("https://androidx.dev/storage/compose-compiler/repository/")
            content {
                includeModule("androidx.compose.compiler", "compiler")
            }
        }
    }
}

plugins {
    id("com.gradle.enterprise")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.android.settings")
}

rootProject.name = "kstreamlined-mobile"

// KMP
include(":kmp:feed-datasource")
include(":kmp:feed-sync:common")
include(":kmp:feed-sync:runtime")
include(":kmp:feed-sync:testing")
include(":kmp:networking:common")
include(":kmp:networking:cloud")
include(":kmp:networking:edge")
include(":kmp:networking:mock")
include(":kmp:networking:testing")
include(":kmp:database")
include(":kmp:database-testing")
include(":kmp:model")
include(":kmp:presentation:common")
include(":kmp:presentation:content-viewer")
include(":kmp:presentation:home")
include(":kmp:presentation:kotlin-weekly-issue")
include(":kmp:presentation:saved-for-later")
include(":kmp:presentation:talking-kotlin-episode")
include(":kmp:pretty-time")

val isXCFrameworkBuild = startParameter.taskNames.any { it.endsWith("XCFramework") }
if (!isXCFrameworkBuild) {
    // Android
    includeProject(":app", "android/app")
    includeProject(":feature:common", "android/feature/common")
    includeProject(":feature:content-viewer", "android/feature/content-viewer")
    includeProject(":feature:home", "android/feature/home")
    includeProject(":feature:kotlin-weekly-issue", "android/feature/kotlin-weekly-issue")
    includeProject(":feature:saved-for-later", "android/feature/saved-for-later")
    includeProject(":feature:talking-kotlin-episode", "android/feature/talking-kotlin-episode")
    includeProject(":foundation:common-ui:feed", "android/foundation/common-ui/feed")
    includeProject(":foundation:designsystem", "android/foundation/designsystem")
    includeProject(":foundation:compose-utils", "android/foundation/compose-utils")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(providers.environmentVariable("CI").orNull == "true")
    }
}

settings.extensions.configure<SettingsExtension> {
    execution {
        profiles {
            create("local") {
                r8 {
                    jvmOptions += "-Xms8g -Xmx8g -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError".split(" ")
                    runInSeparateProcess = true
                }
            }
            create("ci") {
                r8 {
                    jvmOptions += "-Xms4g -Xmx4g -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError".split(" ")
                    runInSeparateProcess = true
                }
            }
            defaultProfile = "local"
        }
    }
}
