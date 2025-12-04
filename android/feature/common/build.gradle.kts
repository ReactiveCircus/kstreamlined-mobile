import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.feature.common") {
        androidResources()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.browser)
            implementation(libs.kermit)
        }
    }
}
