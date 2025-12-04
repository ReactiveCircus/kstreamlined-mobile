import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.savedforlater") {
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":feature:common"))
            implementation(project(":core:common-ui:feed"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:saved-for-later"))
        }
    }
}
