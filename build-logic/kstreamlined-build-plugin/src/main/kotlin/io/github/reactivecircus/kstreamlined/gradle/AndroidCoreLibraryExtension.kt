@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidLibraryExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.gradle.internal.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.internal.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.internal.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKsp
import io.github.reactivecircus.kstreamlined.gradle.internal.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.internal.configureScreenshotTest
import io.github.reactivecircus.kstreamlined.gradle.internal.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import javax.inject.Inject

/**
 * Entry point for configuring an Android core library module.
 */
@KStreamlinedExtensionMarker
public interface AndroidCoreLibraryExtension {
    /**
     * Enable Compose.
     */
    public fun compose()

    /**
     * Enable Hilt by adding the `hilt-compiler` using KSP and adding the `hilt-android` runtime dependency.
     */
    public fun hilt()

    /**
     * Enable Android resources.
     */
    public fun androidResources()

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

internal abstract class AndroidCoreLibraryExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
) : AndroidCoreLibraryExtension, TopLevelExtension {
    private var composeEnabled: Boolean = false

    private var hiltEnabled: Boolean = false

    private var androidResourcesEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var screenshotTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.Library>? = null

    override fun compose() {
        composeEnabled = true
    }

    override fun hilt() {
        hiltEnabled = true
    }

    override fun androidResources() {
        androidResourcesEnabled = true
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
            it.configureBuiltInKotlin(this)
        }

        extensions.configure(LibraryExtension::class.java) {
            it.configureAndroidLibraryExtension(
                project = this,
                namespace = namespace,
                enableAndroidResources = androidResourcesEnabled,
            )
            dependenciesBlock?.let { block ->
                configureDependencies(
                    sourceSets = it.sourceSets,
                    dependenciesBlock = block,
                )
            }
        }

        extensions.configure(LibraryAndroidComponentsExtension::class.java) {
            it.configureAndroidLibraryVariants()
        }

        if (composeEnabled) {
            configureCompose(
                jvmTargetEnabled = false,
                androidTargetEnabled = true,
                iosTargetEnabled = false,
            )
        }

        if (hiltEnabled) {
            configureKsp()
            with(dependencies) {
                add("ksp", libs.hilt.compiler)
                add("implementation", libs.hilt.android)
            }
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
}
