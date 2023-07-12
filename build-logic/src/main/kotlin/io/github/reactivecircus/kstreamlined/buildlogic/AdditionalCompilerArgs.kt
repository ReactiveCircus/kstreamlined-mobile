package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.file.DirectoryProperty

internal val additionalCompilerArgs = listOf(
    "-XXLanguage:+InlineClasses",
    "-Xjvm-default=all",
)

internal fun composeCompilerMetricsArgs(buildDir: DirectoryProperty) = buildDir.dir("compose_metrics").map {
    listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$it"
    )
}


internal fun composeCompilerReportsArgs(buildDir: DirectoryProperty) = buildDir.dir("compose_metrics").map {
    listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$it"
    )
}
