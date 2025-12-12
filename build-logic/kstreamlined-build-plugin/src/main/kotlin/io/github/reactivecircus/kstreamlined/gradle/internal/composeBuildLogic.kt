package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.Project
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

/**
 * Apply and configure Compose plugin.
 */
internal fun Project.configureCompose(
    jvmTargetEnabled: Boolean,
    androidTargetEnabled: Boolean,
    iosTargetEnabled: Boolean,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    extensions.configure(ComposeCompilerGradlePluginExtension::class.java) {
        // add compose stability config file
        @Suppress("UnstableApiUsage")
        it.stabilityConfigurationFiles.add(isolated.rootProject.projectDirectory.file("stability_config.conf"))

        // compose compiler metrics and reports
        if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
            it.metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
            it.reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
        }

        // only enable compose compiler for relevant target platforms
        it.targetKotlinPlatforms.set(
            buildSet {
                if (jvmTargetEnabled) add(KotlinPlatformType.jvm)
                if (androidTargetEnabled) add(KotlinPlatformType.androidJvm)
                if (iosTargetEnabled) add(KotlinPlatformType.native)
            },
        )
    }
}
