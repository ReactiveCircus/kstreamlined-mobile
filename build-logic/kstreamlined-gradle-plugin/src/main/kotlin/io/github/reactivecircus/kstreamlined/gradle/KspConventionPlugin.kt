package io.github.reactivecircus.kstreamlined.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.google.devtools.ksp")
        }

        extensions.configure<KspExtension> {
            if (hasHiltCompilerDependency) {
                arg("dagger.fastInit", "enabled")
                arg("dagger.strictMultibindingValidation", "enabled")
                arg("dagger.experimentalDaggerErrorMessages", "enabled")
            }
        }

        // disable ksp tasks for unit tests
        tasks.named {
            it.startsWith("ksp") && it.endsWith("UnitTestKotlin")
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
