package io.github.reactivecircus.chameleon.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinBaseApiPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class ChameleonPlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        target.extensions.create("chameleon", ChameleonExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        project.pluginManager.withPlugin("com.android.base") {
            if (project.plugins.hasPlugin(KotlinBaseApiPlugin::class.java)) {
                project.dependencies.add(
                    "testImplementation",
                    "io.github.reactivecircus.chameleon:chameleon-runtime",
                )
            }
        }
        val extension = project.extensions.getByType(ChameleonExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption(
                    key = "snapshotFunction",
                    value = extension.snapshotFunction.get(),
                ),
            )
        }
    }

    override fun getCompilerPluginId(): String = "io.github.reactivecircus.chameleon.compiler"

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = "io.github.reactivecircus.chameleon",
            artifactId = "chameleon-compiler-plugin",
        )
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}
