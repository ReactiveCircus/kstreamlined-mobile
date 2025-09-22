package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        extensions.configure(ComposeCompilerGradlePluginExtension::class.java) {
            val stabilityConfigFile = layout.projectDirectory.file("compose_stability_config.conf")
            if (stabilityConfigFile.asFile.exists()) {
                it.stabilityConfigurationFiles.add(stabilityConfigFile)
            }
            if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                it.metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                it.reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
            }
            it.targetKotlinPlatforms.set(
                KotlinPlatformType.entries.filterNot { target ->
                    target == KotlinPlatformType.js || target == KotlinPlatformType.wasm
                }
            )
        }
    }
}
