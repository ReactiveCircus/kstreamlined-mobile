package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.tasks.Delete

@Suppress("UnstableApiUsage", "Unused")
internal class KStreamlinedSettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.gradle.lifecycle.beforeProject {
            if (it.isolated.rootProject == it.isolated) {
                context(it) { configureRootProject() }
            }
            if (it.buildFile.exists()) {
                it.pluginManager.apply("kstreamlined.build")
            }
        }
    }

    context(project: Project)
    private fun configureRootProject() {
        // register task for cleaning the build directory in the root project
        project.tasks.register("clean", Delete::class.java) {
            it.delete(project.isolated.rootProject.projectDirectory.file("build"))
        }
    }
}
