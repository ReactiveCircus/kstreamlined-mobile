package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configure<ComposeCompilerGradlePluginExtension> {
            val stabilityConfigFile = layout.projectDirectory.file("compose_stability_config.conf")
            if (stabilityConfigFile.asFile.exists()) {
                stabilityConfigurationFiles.add(stabilityConfigFile)
            }
            if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
            }
            targetKotlinPlatforms.set(
                KotlinPlatformType.values().filterNot {
                    it == KotlinPlatformType.js || it == KotlinPlatformType.wasm
                }
            )
        }
    }
}
