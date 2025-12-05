import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode") {
        unitTests()
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":core:intents"))
            implementation(project(":feature:common"))
            implementation(project(":core:ui:utils"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:talking-kotlin-episode"))
            implementation(libs.androidx.media3.exoplayer)
        }
    }
}
