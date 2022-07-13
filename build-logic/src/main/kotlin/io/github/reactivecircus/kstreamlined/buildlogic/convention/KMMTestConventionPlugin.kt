package io.github.reactivecircus.kstreamlined.buildlogic.convention

import io.github.reactivecircus.kstreamlined.buildlogic.configureKMMTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KMMTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        extensions.configure<KotlinMultiplatformExtension> {
            configureKMMTest(target)
        }
    }
}
