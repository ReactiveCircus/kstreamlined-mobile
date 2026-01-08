package io.github.reactivecircus.kstreamlined.gradle.internal

import dev.zacsweers.metro.gradle.DelicateMetroGradleApi
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Project

/**
 * Apply and configure Metro plugin.
 */
@OptIn(DelicateMetroGradleApi::class)
internal fun Project.configureMetro() {
    pluginManager.apply("dev.zacsweers.metro")
    extensions.configure(MetroPluginExtension::class.java) {
        if (providers.gradleProperty("enableMetroCompilerReports").orNull == "true") {
            it.reportsDestination.set(layout.buildDirectory.dir("metro_reports"))
        }
    }
}
