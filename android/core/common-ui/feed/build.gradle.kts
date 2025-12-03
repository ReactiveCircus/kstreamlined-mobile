import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.commonui.feed") {
        compose()
        screenshotTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":core:designsystem"))
            implementation(project(":core:compose-utils"))
            implementation(project(":kmp:feed-model"))

            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.tracing)
            implementation(libs.coil.compose)
        }
    }
}
