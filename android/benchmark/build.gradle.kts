@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import io.github.reactivecircus.kstreamlined.buildlogic.FlavorDimensions
import io.github.reactivecircus.kstreamlined.buildlogic.ProductFlavors

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
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel6Api35") {
            device = "Pixel 6"
            apiLevel = 35
            systemImageSource = "aosp-atd"
        }
    }
}

baselineProfile {
    managedDevices += "pixel6Api35"
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
