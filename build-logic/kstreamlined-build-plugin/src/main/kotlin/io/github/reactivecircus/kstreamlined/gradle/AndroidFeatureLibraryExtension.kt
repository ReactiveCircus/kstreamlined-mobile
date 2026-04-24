@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidLibraryExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.gradle.internal.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.internal.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.internal.configureMetro
import io.github.reactivecircus.kstreamlined.gradle.internal.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.internal.configureScreenshotTest
import io.github.reactivecircus.kstreamlined.gradle.internal.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import javax.inject.Inject

/**
 * Entry point for configuring an Android feature library module.
 */
@KStreamlinedExtensionMarker
public interface AndroidFeatureLibraryExtension {
    /**
     * Enable RouteBinding by applying the `io.github.reactivecircus.routebinding` plugin.
     */
    public fun routeBinding()

    /**
     * Enable kotlinx.serialization by applying the `org.jetbrains.kotlin.plugin.serialization` plugin.
     */
    public fun serialization()

    /**
     * Enable unit tests.
     */
    public fun unitTests()

    /**
     * Enable screenshot tests.
     */
    public fun screenshotTests()

    /**
     * Configure dependencies.
     */
    public fun dependencies(action: Action<KSDependencies.Android.Library>)
}

internal abstract class AndroidFeatureLibraryExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
) : AndroidFeatureLibraryExtension, TopLevelExtension {
    private var routeBindingEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var screenshotTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.Library>? = null

    override fun routeBinding() {
        routeBindingEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun screenshotTests() {
        screenshotTestsEnabled = true
    }

    override fun dependencies(action: Action<KSDependencies.Android.Library>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        pluginManager.apply("com.android.library")

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureKotlin(this)
        }

        extensions.configure(LibraryExtension::class.java) {
            it.configureAndroidLibraryExtension(
                project = this,
                namespace = namespace,
                enableAndroidResources = true,
            )
            dependenciesBlock?.let { block ->
                configureDependencies(
                    sourceSets = it.sourceSets,
                    dependenciesBlock = block,
                )
            }
        }

        extensions.configure(LibraryAndroidComponentsExtension::class.java) {
            it.configureAndroidLibraryVariants(unitTestsEnabled)
        }

        configureMetro()
        exposeDependenciesForMetroContributionDiscovery()

        configureCompose(
            jvmTargetEnabled = false,
            androidTargetEnabled = true,
            iosTargetEnabled = false,
        )

        @Suppress("UnstableApiUsage")
        with(dependencies) {
            add("implementation", project(":kmp:capsule:inject"))
            add("implementation", project(":core:designsystem"))
            add("implementation", libs.androidx.compose.foundation)
            add("implementation", libs.androidx.compose.ui.tooling)
            add("implementation", libs.androidx.lifecycle.runtime)
            add("implementation", libs.androidx.navigation3.ui)
            add("implementation", libs.androidx.tracing)
            add("implementation", libs.coil.compose)
            add("implementation", libs.kermit)
            add("implementation", libs.kotlinx.coroutines.core)
        }

        if (routeBindingEnabled) {
            pluginManager.apply("io.github.reactivecircus.routebinding")
        }

        if (serializationEnabled) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }

        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configurePowerAssert()
            configureTest()
        }

        if (screenshotTestsEnabled) {
            configureScreenshotTest()
        }

        configureDetekt()
    }

    /**
     * Expose`kmp:presentation:*` project dependencies to downstream (:app module) by
     * changing `implementation` configuration to `api` so that contributed bindings can be discovered.
     */
    private fun Project.exposeDependenciesForMetroContributionDiscovery() {
        configurations.named("implementation").configure { implConfig ->
            implConfig.withDependencies { deps ->
                val presentationDeps = deps
                    .filterIsInstance<ProjectDependency>()
                    .filter { it.path.startsWith(":kmp:presentation:") }
                for (dep in presentationDeps) {
                    deps.remove(dep)
                    configurations.named("api").configure { it.dependencies.add(dep) }
                }
            }
        }
    }
}
