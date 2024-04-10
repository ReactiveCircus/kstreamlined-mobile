package io.github.reactivecircus.kstreamlined.buildlogic.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configure<ComposeCompilerGradlePluginExtension> {
            suppressKotlinVersionCompatibilityCheck.set(the<LibrariesForLibs>().versions.kotlin.get())
            enableIntrinsicRemember.set(true)
            enableNonSkippingGroupOptimization.set(true)
            enableExperimentalStrongSkippingMode.set(true)
            val stabilityConfigFile = layout.projectDirectory.file("compose_stability_config.conf").asFile
            if (stabilityConfigFile.exists()) {
                stabilityConfigurationFile.set(stabilityConfigFile)
            }
            if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
            }
        }
    }
}
