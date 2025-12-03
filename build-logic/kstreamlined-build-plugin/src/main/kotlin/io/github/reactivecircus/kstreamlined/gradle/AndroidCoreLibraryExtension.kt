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
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.libs
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidTopLevelDependencies
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinDependencies
import javax.inject.Inject

/**
 * Entry point for configuring a Android Core library module.
 */
@KStreamlinedExtensionMarker
public interface AndroidCoreLibraryExtension {
    /**
     * Enable Compose.
     */
    public fun compose()

    /**
     * Enable KSP and configure KSP dependencies.
     */
    public fun ksp(action: Action<KspOptions>)

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
    public fun dependencies(action: Action<KotlinDependencies>)
}

internal abstract class AndroidCoreLibraryExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
) : AndroidCoreLibraryExtension, TopLevelExtension {
    private var composeEnabled: Boolean = false

    private var kspOptions: KspOptionsImpl? = null

    private var androidResourcesEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var screenshotTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KotlinDependencies>? = null

    override fun compose() {
        composeEnabled = true
    }

    override fun ksp(action: Action<KspOptions>) {
        if (kspOptions == null) {
            kspOptions = project.objects.newInstance(KspOptionsImpl::class.java)
        }
        action.execute(kspOptions!!)
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

    override fun dependencies(action: Action<KotlinDependencies>) {
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
                configureAndroidTopLevelDependencies(
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

        val kspDependencies = kspOptions?.kspDependencies
        if (!kspDependencies.isNullOrEmpty()) {
            configureKsp(kspDependencies)
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

public interface KspOptions {
    public fun add(dependency: Any)
}

internal abstract class KspOptionsImpl : KspOptions {
    private val _kspDependencies = mutableSetOf<Any>()
    val kspDependencies: Set<Any> = _kspDependencies

    override fun add(dependency: Any) {
        _kspDependencies.add(dependency)
    }
}
