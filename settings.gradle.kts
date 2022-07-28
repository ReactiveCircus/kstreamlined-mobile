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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "kstreamlined-mobile"

val isXCFrameworkBuild = startParameter.taskNames.any { it.endsWith("XCFramework") }

// KMM
include(":kmm:apollo-models")

if (!isXCFrameworkBuild) {
    // Android
    includeProject(":app", "android/app")
    includeProject(":ui-common", "android/ui/ui-common")
}

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}
