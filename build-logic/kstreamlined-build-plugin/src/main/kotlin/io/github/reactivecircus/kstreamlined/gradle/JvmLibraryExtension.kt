@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
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
    public fun dependencies(action: Action<KSDependencies.Jvm>)
}

internal abstract class JvmLibraryExtensionImpl @Inject constructor(
    private val project: Project,
) : JvmLibraryExtension, TopLevelExtension {
    private var composeEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Jvm>? = null

    override fun compose() {
        composeEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun dependencies(action: Action<KSDependencies.Jvm>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("com.android.lint")

        extensions.configure(KotlinJvmProjectExtension::class.java) {
            it.configureKotlin(this)

            dependenciesBlock?.let { block ->
                configureDependencies(
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
