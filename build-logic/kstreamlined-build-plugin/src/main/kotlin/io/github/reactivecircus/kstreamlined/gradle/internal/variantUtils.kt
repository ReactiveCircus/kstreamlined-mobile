package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.Project

internal fun Project.shouldEnableVariant(variantName: String): Boolean = variantName in listOf(
    "devDebug",
    "demoDebug",
    "mockDebug",
    "prodRelease",
) ||
    // nonMinified variant for baseline profile generation
    isGeneratingBaselineProfile && variantName == "devNonMinifiedRelease" ||
    // benchmark variants
    isRunningBenchmark && variantName in listOf("devRelease", "devBenchmarkRelease")

private val Project.isGeneratingBaselineProfile: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("generateBaselineProfile", ignoreCase = true)
    }

private val Project.isRunningBenchmark: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("connectedBenchmarkReleaseAndroidTest", ignoreCase = true)
    }
