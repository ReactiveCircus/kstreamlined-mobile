package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

@Suppress("UnstableApiUsage", "Unused")
internal class KStreamlinedBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.isolated.rootProject == target.isolated) {
            context(target) { configureRootProject() }
            return
        }
        val extension = target.extensions.create(
            KStreamlinedExtension::class.java,
            "kstreamlined",
            KStreamlinedExtensionImpl::class.java,
        ) as KStreamlinedExtensionImpl

        target.afterEvaluate {
            extension.validate()
        }
    }

    context(project: Project)
    private fun configureRootProject() {
        project.pluginManager.apply("com.squareup.invert")

        // register task for cleaning the build directory in the root project
        project.tasks.register("clean", Delete::class.java) {
            it.delete(project.isolated.rootProject.projectDirectory.file("build"))
        }
    }
}
