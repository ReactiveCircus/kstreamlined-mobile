@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidLibraryExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKsp
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureScreenshotTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import javax.inject.Inject

/**
 * Entry point for configuring an Android feature library module.
 */
@KStreamlinedExtensionMarker
public interface AndroidFeatureLibraryExtension {
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
    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var screenshotTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.Library>? = null

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
            it.configureAndroidLibraryVariants()
        }

        configureCompose(
            jvmTargetEnabled = false,
            androidTargetEnabled = true,
            iosTargetEnabled = false,
        )

        configureKsp()

        with(dependencies) {
            add("implementation", project(":core:designsystem"))
            add("ksp", libs.hilt.compiler)
            add("implementation", libs.hilt.android)
            add("implementation", libs.androidx.compose.foundation)
            add("implementation", libs.androidx.compose.ui.tooling)
            add("implementation", libs.androidx.lifecycle.runtimeCompose)
            add("implementation", libs.androidx.lifecycle.viewmodelCompose)
            add("implementation", libs.androidx.hilt.lifecycleViewmodelCompose)
            add("implementation", libs.androidx.tracing)
            add("implementation", libs.coil.compose)
            add("implementation", libs.kermit)
            add("implementation", libs.kotlinx.coroutines.core)
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
