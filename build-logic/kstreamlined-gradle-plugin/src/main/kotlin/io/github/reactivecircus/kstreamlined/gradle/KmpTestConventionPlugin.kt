package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKmpTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

internal class KmpTestConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("org.jetbrains.kotlin.plugin.power-assert")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            configureKmpTest()
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        extensions.configure<PowerAssertGradleExtension> {
            functions.set(
                listOf(
                    "kotlin.assert",
                    "kotlin.test.assertEquals",
                    "kotlin.test.assertTrue",
                    "kotlin.test.assertFalse",
                    "kotlin.test.assertNull",
                )
            )
        }
    }
}
