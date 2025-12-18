@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import FlavorDimensions
import androidx.baselineprofile.gradle.producer.BaselineProfileProducerExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestExtension
import com.android.build.api.variant.TestAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidTestExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.internal.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import javax.inject.Inject

/**
 * Entry point for configuring an Android benchmark module.
 */
@Suppress("UnstableApiUsage")
@KStreamlinedExtensionMarker
public interface AndroidBenchmarkExtension {
    /**
     * Create a new managed virtual device with the provided [name].
     */
    public fun managedVirtualDevice(name: String, action: Action<ManagedVirtualDevice>)

    /**
     * Enable baseline profile generation.
     */
    public fun produceBaselineProfile(vararg devices: String)

    /**
     * Configure dependencies.
     */
    public fun dependencies(action: Action<KSDependencies.Android.Test>)
}

@Suppress("UnstableApiUsage")
internal abstract class AndroidBenchmarkExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
    private val targetProjectPath: String,
    private val environment: String,
    private val targetAppIdKey: String,
    private val testMinSdk: Int,
) : AndroidBenchmarkExtension, TopLevelExtension {
    private val managedVirtualDeviceConfigs: MutableMap<String, Action<ManagedVirtualDevice>> = mutableMapOf()

    private val baselineProfileDevices: MutableList<String> = mutableListOf()

    private var dependenciesBlock: Action<KSDependencies.Android.Test>? = null

    override fun managedVirtualDevice(name: String, action: Action<ManagedVirtualDevice>) {
        managedVirtualDeviceConfigs[name] = action
    }

    override fun produceBaselineProfile(vararg devices: String) {
        baselineProfileDevices += devices
    }

    override fun dependencies(action: Action<KSDependencies.Android.Test>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        pluginManager.apply("com.android.test")

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureBuiltInKotlin(this, enableExplicitApi = false)
        }

        extensions.configure(TestExtension::class.java) {
            it.configureAndroidTestExtension(
                project = this,
                namespace = namespace,
            )
            it.defaultConfig {
                missingDimensionStrategy(FlavorDimensions.Environment, environment)
                minSdk = testMinSdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testInstrumentationRunnerArguments["androidx.benchmark.fullTracing.enable"] = "true"
                // TODO remove once https://issuetracker.google.com/issues/435589290 is fixed
                testInstrumentationRunnerArguments["androidx.benchmark.killExistingPerfettoRecordings"] = "false"
            }
            it.targetProjectPath = targetProjectPath
            it.experimentalProperties["android.experimental.self-instrumenting"] = true
            with(it.testOptions.managedDevices.allDevices) {
                managedVirtualDeviceConfigs.forEach { (name, action) ->
                    create(name, ManagedVirtualDevice::class.java, action)
                }
            }

            dependenciesBlock?.let { block ->
                configureDependencies(
                    sourceSets = it.sourceSets,
                    dependenciesBlock = block,
                )
            }
        }

        extensions.configure(TestAndroidComponentsExtension::class.java) {
            it.onVariants { variant ->
                val artifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
                variant.instrumentationRunnerArguments.put(
                    targetAppIdKey,
                    variant.testedApks.map { dir -> artifactsLoader.load(dir)!!.applicationId },
                )
            }
        }

        if (baselineProfileDevices.isNotEmpty()) {
            pluginManager.apply("androidx.baselineprofile")
            extensions.configure(BaselineProfileProducerExtension::class.java) {
                it.managedDevices += baselineProfileDevices
                it.useConnectedDevices = false
            }
        }

        with(dependencies) {
            add("implementation", libs.androidx.test.runner)
            add("implementation", libs.androidx.test.rules)
            add("implementation", libs.androidx.test.junit)
            add("implementation", libs.androidx.test.uiautomator)
            add("implementation", libs.androidx.benchmark.macroJunit)
            add("implementation", libs.androidx.tracing.perfetto)
            add("implementation", libs.androidx.tracing.perfetto.binary)
        }

        configurations.configureEach { configuration ->
            configuration.resolutionStrategy.eachDependency { details ->
                if (details.requested.name.startsWith("tracing-perfetto")) {
                    details.useVersion(libs.versions.androidx.tracing.perfetto.get())
                }
            }
        }

        configureDetekt()
    }
}
