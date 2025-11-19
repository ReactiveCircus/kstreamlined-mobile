package io.github.reactivecircus.chameleon.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinBaseApiPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class ChameleonGradlePlugin : KotlinCompilerPluginSupportPlugin {
    private var version: String? = null

    override fun apply(target: Project) {
        version = target.version as String
        target.extensions.create("chameleon", ChameleonExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.compileTaskProvider.configure {
            it.compilerOptions.freeCompilerArgs.add(
                "-Xcompiler-plugin-order=$ChameleonCompilerPluginId>$BurstCompilerPluginId",
            )
        }
        val project = kotlinCompilation.target.project
        project.pluginManager.withPlugin("com.android.base") {
            if (project.plugins.hasPlugin(KotlinBaseApiPlugin::class.java)) {
                project.dependencies.add(
                    "testImplementation",
                    "io.github.reactivecircus.chameleon:chameleon-runtime:$version",
                )
            }
        }
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.dependencies.add(
                "testImplementation",
                "io.github.reactivecircus.chameleon:chameleon-runtime:$version",
            )
        }
        val extension = project.extensions.getByType(ChameleonExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption(
                    key = "snapshotFunction",
                    value = extension.snapshotFunction.get(),
                ),
                SubpluginOption(
                    key = "themeVariantEnum",
                    value = extension.themeVariantEnum.get(),
                ),
            )
        }
    }

    override fun getCompilerPluginId(): String = ChameleonCompilerPluginId

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = "io.github.reactivecircus.chameleon",
            artifactId = "chameleon-compiler-plugin",
            version = version,
        )
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.name.contains("test", ignoreCase = true)
}

private const val ChameleonCompilerPluginId = "io.github.reactivecircus.chameleon.compiler"
private const val BurstCompilerPluginId = "app.cash.burst.kotlin"
