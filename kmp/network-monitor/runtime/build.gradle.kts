import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined.kmp.android-and-ios")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:network-monitor:common"))
    }

    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "io.github.reactivecircus.kmp.networkmonitor.runtime"
    }
}
