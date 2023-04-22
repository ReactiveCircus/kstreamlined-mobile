package io.github.reactivecircus.kstreamlined.buildlogic.convention

import io.github.reactivecircus.kstreamlined.buildlogic.isCiBuild
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

@Suppress("MagicNumber")
internal class KaptConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.kapt")
        }

        extensions.configure<KaptExtension> {
            javacOptions {
                if (hasHiltCompilerDependency) {
                    option("-Adagger.fastInit=enabled")
                    option("-Adagger.strictMultibindingValidation=enabled")
                    option("-Adagger.experimentalDaggerErrorMessages=enabled")
                    if (isCiBuild) {
                        option("-Xmaxerrs", 500)
                    }
                }
            }
        }

        // disable kapt tasks for unit tests
        tasks.matching {
            it.name.startsWith("kapt") && it.name.endsWith("UnitTestKotlin")
        }.configureEach {
            enabled = false
        }
    }
}

private val Project.hasHiltCompilerDependency: Boolean
    get() = configurations.any {
        it.dependencies.any { dependency ->
            dependency.name == "hilt-compiler"
        }
    }
