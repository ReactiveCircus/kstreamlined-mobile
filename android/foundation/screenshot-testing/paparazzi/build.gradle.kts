import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
    id("kstreamlined.compose")
}

kstreamlined {
    jvmLibrary {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(libs.androidx.compose.runtime)
            implementation(libs.paparazzi)
        }
    }
}
