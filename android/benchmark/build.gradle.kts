@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.FlavorDimensions
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.ProductFlavors

plugins {
    id("kstreamlined.android.test")
    id("androidx.baselineprofile")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.benchmark"

    defaultConfig {
        missingDimensionStrategy(FlavorDimensions.Environment, ProductFlavors.Dev)
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.fullTracing.enable"] = "true"
        testInstrumentationRunnerArguments["androidx.benchmark.killExistingPerfettoRecordings"] = "false" // remove once https://issuetracker.google.com/issues/323601788 is fixed
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel9Api35") {
            device = "Pixel 9"
            apiLevel = 35
            systemImageSource = "aosp-atd"
        }
    }
}

baselineProfile {
    managedDevices += "pixel9Api35"
    useConnectedDevices = false
}

androidComponents {
    onVariants { variant ->
        val artifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
        variant.instrumentationRunnerArguments.put(
            "targetAppId",
            variant.testedApks.map { artifactsLoader.load(it)!!.applicationId }
        )
    }
}

dependencies {
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macroJunit)
    implementation(libs.androidx.tracing.perfetto)
    implementation(libs.androidx.tracing.perfetto.binary)
    implementation(libs.kermit)
}
