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
    }
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

if (!isXCFrameworkBuild) {
    // Android
    includeProject(":app", "android/app")
    includeProject(":ui-common", "android/ui/ui-common")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}
