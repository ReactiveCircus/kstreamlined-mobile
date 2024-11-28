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
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            content {
                includeGroup("com.google.dagger")
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
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            content {
                includeGroup("com.google.dagger")
            }
        }
    }
}

plugins {
    id("com.gradle.develocity")
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.android.settings")
}

rootProject.name = "kstreamlined-mobile"

// KMP
include(":kmp:feed-datasource")
include(":kmp:feed-sync:common")
include(":kmp:feed-sync:runtime")
include(":kmp:feed-sync:testing")
include(":kmp:remote:common")
include(":kmp:remote:cloud")
include(":kmp:remote:edge")
include(":kmp:remote:mock")
include(":kmp:remote:testing")
include(":kmp:database")
include(":kmp:database-testing")
include(":kmp:network-monitor:common")
include(":kmp:network-monitor:runtime")
include(":kmp:network-monitor:testing")
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
    includeProject(":benchmark", "android/benchmark")
    includeProject(":feature:common", "android/feature/common")
    includeProject(":feature:content-viewer", "android/feature/content-viewer")
    includeProject(":feature:home", "android/feature/home")
    includeProject(":feature:kotlin-weekly-issue", "android/feature/kotlin-weekly-issue")
    includeProject(":feature:saved-for-later", "android/feature/saved-for-later")
    includeProject(":feature:talking-kotlin-episode", "android/feature/talking-kotlin-episode")
    includeProject(":foundation:common-ui:feed", "android/foundation/common-ui/feed")
    includeProject(":foundation:designsystem", "android/foundation/designsystem")
    includeProject(":foundation:compose-utils", "android/foundation/compose-utils")
    includeProject(":foundation:scheduled-work", "android/foundation/scheduled-work")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
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
