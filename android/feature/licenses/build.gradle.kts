import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.licenses") {
        unitTests()
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":kmp:presentation:common"))
            implementation(project(":feature:common"))
            implementation(project(":kmp:app-info"))

            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
