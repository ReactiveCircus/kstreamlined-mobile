package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Suppress("Unused")
internal class KStreamlinedSettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        @Suppress("UnstableApiUsage")
        settings.gradle.lifecycle.beforeProject {
            if (it.buildFile.exists()) {
                it.pluginManager.apply("kstreamlined.build")
            }
        }
    }
}
