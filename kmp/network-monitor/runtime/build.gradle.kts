import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined.kmp.android-and-ios")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:network-monitor:common"))
    }

    android {
        namespace = "io.github.reactivecircus.kmp.networkmonitor.runtime"
    }
}
