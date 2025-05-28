package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

@Suppress("UnstableApiUsage", "Unused")
internal class KStreamlinedBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.isolated.rootProject == target.isolated) {
            configureRootProject(target)
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

    private fun configureRootProject(target: Project) {
        target.pluginManager.apply("com.squareup.invert")

        // register task for cleaning the build directory in the root project
        target.tasks.register("clean", Delete::class.java) {
            @Suppress("UnstableApiUsage")
            it.delete(target.isolated.rootProject.projectDirectory.file("build"))
        }
    }
}
