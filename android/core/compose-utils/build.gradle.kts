import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.composeutils") {
        compose()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(libs.androidx.compose.foundation)
        }
    }
}
