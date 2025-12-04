import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.home") {
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":feature:common"))
            implementation(project(":core:common-ui:feed"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:feed-sync:common"))
            implementation(project(":kmp:presentation:home"))
            implementation(libs.androidx.activity.compose)
        }
    }
}
