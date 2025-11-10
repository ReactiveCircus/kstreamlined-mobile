package io.github.reactivecircus.kstreamlined.gradle.convention

import com.google.devtools.ksp.gradle.KspExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure(KspExtension::class.java) {
            if (hasHiltCompilerDependency) {
                it.arg("dagger.fastInit", "enabled")
                it.arg("dagger.strictMultibindingValidation", "enabled")
                it.arg("dagger.experimentalDaggerErrorMessages", "enabled")
            }
        }

        // disable ksp tasks for unit tests
        tasks.named {
            it.startsWith("ksp") && it.endsWith("UnitTestKotlin")
        }.configureEach {
            it.enabled = false
        }

        // TODO remove once dagger with kotlin-metadata-jvm 2.3.0 is released: https://github.com/google/dagger/issues/5001
        configurations.configureEach { configuration ->
            configuration.resolutionStrategy {
                it.force("org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.versions.kotlin.get()}")
            }
        }
    }
}

private val Project.hasHiltCompilerDependency: Boolean
    get() = configurations.any {
        it.dependencies.any { dependency ->
            dependency.name == "hilt-compiler"
        }
    }
