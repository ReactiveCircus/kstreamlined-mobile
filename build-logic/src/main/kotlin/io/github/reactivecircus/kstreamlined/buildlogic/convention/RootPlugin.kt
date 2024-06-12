package io.github.reactivecircus.kstreamlined.buildlogic.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.register

internal class RootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.squareup.invert")

        // register task for cleaning the build directory in the root project
        target.tasks.register<Delete>("clean") {
            delete(project.rootProject.layout.buildDirectory)
        }
    }
}
