package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import javax.inject.Inject

/**
 * Entry point for configuring a JVM library module.
 */
@KStreamlinedExtensionMarker
public interface JvmLibraryExtension {
    /**
     * Enable unit tests.
     */
    public fun unitTests()

    /**
     * Configure dependencies for `main` source set.
     */
    public fun mainDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure dependencies for `test` source set.
     */
    public fun testDependencies(action: Action<KotlinDependencyHandler>)
}

internal abstract class JvmLibraryExtensionImpl @Inject constructor(
    private val project: Project,
) : JvmLibraryExtension {
    private var unitTestsEnabled: Boolean = false

    private var mainDependenciesBlock: Action<KotlinDependencyHandler>? = null
    private var testDependenciesBlock: Action<KotlinDependencyHandler>? = null

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun mainDependencies(action: Action<KotlinDependencyHandler>) {
        mainDependenciesBlock = action
    }

    override fun testDependencies(action: Action<KotlinDependencyHandler>) {
        testDependenciesBlock = action
    }

    internal fun evaluate() = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("com.android.lint")

        extensions.configure(KotlinJvmProjectExtension::class.java) {
            it.configureKotlin(this)
            it.configureDependencies()
        }

        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configureTest()
        }

        configureDetekt()
    }

    private fun KotlinJvmProjectExtension.configureDependencies() {
        mainDependenciesBlock?.let {
            sourceSets.getByName("main").dependencies(it)
        }
        testDependenciesBlock?.let {
            sourceSets.getByName("test").dependencies(it)
        }
    }
}
