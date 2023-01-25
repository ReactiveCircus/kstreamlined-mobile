pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        google()
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

rootProject.name = "kstreamlined-mobile"

val isXCFrameworkBuild = startParameter.taskNames.any { it.endsWith("XCFramework") }

// KMM
include(":kmm:apollo-models")
include(":kmm:data-common")
include(":kmm:data-runtime-cloud")
include(":kmm:data-runtime-edge")
include(":kmm:data-testing")
include(":kmm:core-utils")
include(":kmm:test-utils")

if (!isXCFrameworkBuild) {
    // Android
    includeProject(":app", "android/app")
    includeProject(":ui-common", "android/ui/ui-common")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}
