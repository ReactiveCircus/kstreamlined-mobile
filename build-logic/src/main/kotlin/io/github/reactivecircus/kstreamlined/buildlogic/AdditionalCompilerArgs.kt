package io.github.reactivecircus.kstreamlined.buildlogic

import java.io.File

internal val additionalCompilerArgs = listOf(
    "-XXLanguage:+InlineClasses",
    "-Xjvm-default=all",
)

internal fun composeCompilerMetricsArgs(buildDir: File) = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${buildDir.resolve("compose_metrics")}"
)

internal fun composeCompilerReportsArgs(buildDir: File) = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${buildDir.resolve("compose_metrics")}"
)
