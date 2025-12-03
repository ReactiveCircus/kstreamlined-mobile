package io.github.reactivecircus.kstreamlined.gradle.convention

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKsp
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureKsp()
    }
}
