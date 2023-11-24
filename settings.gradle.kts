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
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.android.settings")
}

rootProject.name = "kstreamlined-mobile"

// KMP
include(":kmp:data")
include(":kmp:feed-datasource:common")
include(":kmp:feed-datasource:cloud")
include(":kmp:feed-datasource:edge")
include(":kmp:feed-datasource:testing")
include(":kmp:persistence")
include(":kmp:model")
include(":kmp:core-utils")
include(":kmp:test-utils")

val isXCFrameworkBuild = startParameter.taskNames.any { it.endsWith("XCFramework") }
if (!isXCFrameworkBuild) {
    // Android
    includeProject(":app", "android/app")
    includeProject(":feature:common", "android/feature/common")
    includeProject(":feature:home", "android/feature/home")
    includeProject(":common-ui:feed", "android/common-ui/feed")
    includeProject(":designsystem", "android/designsystem")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

settings.extensions.configure<SettingsExtension> {
    execution {
        profiles {
            create("local") {
                r8 {
                    r8.jvmOptions += "-Xms8g -Xmx8g -XX:+UseZGC -XX:+ZGenerational -XX:+HeapDumpOnOutOfMemoryError".split(" ")
                    r8.runInSeparateProcess = true
                }
            }
            create("ci") {
                r8 {
                    r8.jvmOptions += "-Xms4g -Xmx4g -XX:+UseZGC -XX:+ZGenerational -XX:+HeapDumpOnOutOfMemoryError".split(" ")
                    r8.runInSeparateProcess = true
                }
            }
            defaultProfile = "local"
        }
    }
}
