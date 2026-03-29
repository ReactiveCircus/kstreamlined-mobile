package io.github.reactivecircus.routebinding.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinBaseApiPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class RouteBindingGradlePlugin : KotlinCompilerPluginSupportPlugin {
    private var version: String? = null

    override fun apply(target: Project) {
        version = target.version as String
        target.extensions.create("routeBinding", RouteBindingExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.compileTaskProvider.configure {
            it.compilerOptions.freeCompilerArgs.add(
                "-Xcompiler-plugin-order=$RouteBindingCompilerPluginId>$ComposeCompilerPluginId",
            )
        }
        val project = kotlinCompilation.target.project
        project.pluginManager.withPlugin("com.android.base") {
            if (project.plugins.hasPlugin(KotlinBaseApiPlugin::class.java)) {
                project.dependencies.add(
                    "implementation",
                    "io.github.reactivecircus.routebinding:routebinding-runtime:$version",
                )
            }
        }
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.dependencies.add(
                "implementation",
                "io.github.reactivecircus.routebinding:routebinding-runtime:$version",
            )
        }
        return project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String = RouteBindingCompilerPluginId

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = "io.github.reactivecircus.routebinding",
            artifactId = "routebinding-compiler-plugin",
            version = version,
        )
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}

private const val RouteBindingCompilerPluginId = "io.github.reactivecircus.routebinding.compiler"
private const val ComposeCompilerPluginId = "androidx.compose.compiler.plugins.kotlin"
