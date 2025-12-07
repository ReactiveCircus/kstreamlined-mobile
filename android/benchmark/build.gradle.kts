import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidBenchmark(
        namespace = "io.github.reactivecircus.kstreamlined.android.benchmark",
        targetProjectPath = ":app",
        environment = ProductFlavors.Dev,
        targetAppIdKey = "targetAppId",
        minSdk = 28,
    ) {
        @Suppress("UnstableApiUsage")
        managedVirtualDevice("pixel9Api35") {
            device = "Pixel 9"
            apiLevel = 35
            systemImageSource = "aosp-atd"
        }

        produceBaselineProfile("pixel9Api35")

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(libs.kermit)
        }
    }
}
