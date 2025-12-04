import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.settings") {
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":feature:common"))
            implementation(project(":kmp:settings-datasource"))
            implementation(project(":kmp:presentation:settings"))
            implementation(project(":kmp:app-info"))
        }
    }
}
