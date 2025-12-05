import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue") {
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":core:launcher"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":feature:common"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:kotlin-weekly-issue"))
        }
    }
}
