package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

internal class RootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.squareup.invert")

        // register task for cleaning the build directory in the root project
        target.tasks.register("clean", Delete::class.java) {
            @Suppress("UnstableApiUsage")
            it.delete(target.isolated.rootProject.projectDirectory.file("build"))
        }
    }
}
