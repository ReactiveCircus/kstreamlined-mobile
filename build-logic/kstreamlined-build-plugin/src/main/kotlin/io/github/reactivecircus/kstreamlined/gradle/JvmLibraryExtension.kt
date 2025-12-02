@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.libs
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyCollector
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinDependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import javax.inject.Inject

/**
 * Entry point for configuring a JVM library module.
 */
@KStreamlinedExtensionMarker
public interface JvmLibraryExtension {
    /**
     * Enable Compose.
     */
    public fun compose()

    /**
     * Enable kotlinx.serialization by applying the `org.jetbrains.kotlin.plugin.serialization` plugin.
     */
    public fun serialization()

    /**
     * Enable unit tests.
     */
    public fun unitTests()

    /**
     * Configure dependencies.
     */
    public fun dependencies(action: Action<KotlinDependencies>)
}

internal abstract class JvmLibraryExtensionImpl @Inject constructor(
    private val project: Project,
) : JvmLibraryExtension, TopLevelExtension {
    private var composeEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KotlinDependencies>? = null

    override fun compose() {
        composeEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun dependencies(action: Action<KotlinDependencies>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("com.android.lint")

        extensions.configure(KotlinJvmProjectExtension::class.java) {
            it.configureKotlin(this)

            dependenciesBlock?.let { block ->
                configureTopLevelDependencies(
                    sourceSets = it.sourceSets,
                    dependenciesBlock = block,
                )
            }
        }

        if (composeEnabled) {
            configureCompose(
                jvmTargetEnabled = true,
                androidTargetEnabled = false,
                iosTargetEnabled = false,
            )
        }

        if (serializationEnabled) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }
        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configurePowerAssert()
            configureTest()
        }

        configureDetekt()
    }
}

@Suppress("UnstableApiUsage")
private fun Project.configureTopLevelDependencies(
    sourceSets: NamedDomainObjectContainer<KotlinSourceSet>,
    dependenciesBlock: Action<KotlinDependencies>,
) {
    val kotlinDependencies = project.objects.newInstance(KotlinDependenciesImpl::class.java)

    val main = sourceSets.getByName("main")
    val test = sourceSets.getByName("test")

    infix fun DependencyCollector.wireWith(configurationName: String) {
        val configuration = project.configurations.getByName(configurationName)
        configuration.fromDependencyCollector(this)
    }

    kotlinDependencies.api wireWith main.apiConfigurationName
    kotlinDependencies.implementation wireWith main.implementationConfigurationName
    kotlinDependencies.compileOnly wireWith main.compileOnlyConfigurationName
    kotlinDependencies.runtimeOnly wireWith main.runtimeOnlyConfigurationName

    kotlinDependencies.testImplementation wireWith test.implementationConfigurationName
    kotlinDependencies.testCompileOnly wireWith test.compileOnlyConfigurationName
    kotlinDependencies.testRuntimeOnly wireWith test.runtimeOnlyConfigurationName

    dependenciesBlock.execute(kotlinDependencies)
}

private abstract class KotlinDependenciesImpl : KotlinDependencies {
    override fun kotlin(module: String) = kotlin(module, null)

    override fun kotlin(module: String, version: String?): Dependency = project.dependencyFactory
        .create("org.jetbrains.kotlin", "kotlin-$module", version)
}
