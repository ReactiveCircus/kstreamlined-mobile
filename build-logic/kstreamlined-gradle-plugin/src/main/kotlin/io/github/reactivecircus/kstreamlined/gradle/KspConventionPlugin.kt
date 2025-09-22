package io.github.reactivecircus.kstreamlined.gradle

import com.google.devtools.ksp.gradle.KspExtension
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
    }
}

private val Project.hasHiltCompilerDependency: Boolean
    get() = configurations.any {
        it.dependencies.any { dependency ->
            dependency.name == "hilt-compiler"
        }
    }
