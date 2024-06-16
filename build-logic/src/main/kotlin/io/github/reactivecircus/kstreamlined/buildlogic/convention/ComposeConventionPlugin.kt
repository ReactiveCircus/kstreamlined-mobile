package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configure<ComposeCompilerGradlePluginExtension> {
            enableStrongSkippingMode.set(true)
            val stabilityConfigFile = layout.projectDirectory.file("compose_stability_config.conf").asFile
            if (stabilityConfigFile.exists()) {
                stabilityConfigurationFile.set(stabilityConfigFile)
            }
            if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
            }
            targetKotlinPlatforms.set(setOf(KotlinPlatformType.androidJvm))
        }

        // TODO remove once https://issuetracker.google.com/issues/344057003 is fixed
        plugins.withId("com.android.application") {
            configure<BaseAppModuleExtension> {
                buildFeatures.compose = true
            }
        }
        plugins.withId("com.android.library") {
            configure<LibraryExtension> {
                buildFeatures.compose = true
            }
        }
    }
}
