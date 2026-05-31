package io.github.reactivecircus.kstreamlined.gradle.internal

import isGeneratingBaselineProfile
import isRunningBenchmark
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
