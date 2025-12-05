import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.launcher") {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.browser)
            implementation(libs.kermit)
        }
    }
}
