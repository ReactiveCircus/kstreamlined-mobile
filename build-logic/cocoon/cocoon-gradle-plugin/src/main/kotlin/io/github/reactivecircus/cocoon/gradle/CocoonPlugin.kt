package io.github.reactivecircus.cocoon.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class CocoonPlugin : KotlinCompilerPluginSupportPlugin {
    private var version: String? = null

    override fun apply(target: Project) {
        version = target.version as String
        target.extensions.create("cocoon", CocoonExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(CocoonExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption(
                    key = "annotation",
                    value = extension.annotation.get(),
                ),
                SubpluginOption(
                    key = "wrappingFunction",
                    value = extension.wrappingFunction.get(),
                ),
            )
        }
    }

    override fun getCompilerPluginId(): String = "io.github.reactivecircus.cocoon.compiler"

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = "io.github.reactivecircus.cocoon",
            artifactId = "cocoon-compiler-plugin",
            version = version,
        )
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}
