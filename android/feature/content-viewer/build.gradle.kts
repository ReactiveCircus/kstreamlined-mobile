import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.contentviewer") {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":core:launcher"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:content-viewer"))
            implementation(libs.androidx.core)
            implementation(libs.androidx.activity.compose)
        }
    }
}
