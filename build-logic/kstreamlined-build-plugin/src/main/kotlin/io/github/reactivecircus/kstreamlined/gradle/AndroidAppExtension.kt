@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import ProductFlavors
import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import app.cash.licensee.LicenseeExtension
import app.cash.licensee.UnusedAction
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.triplet.gradle.play.PlayPublisherExtension
import com.google.firebase.appdistribution.gradle.tasks.UploadDistributionTask
import com.google.gms.googleservices.GoogleServicesPlugin
import io.github.reactivecircus.appversioning.AppVersioningExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKsp
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import isCiBuild
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import java.io.File
import javax.inject.Inject

/**
 * Entry point for configuring an Android application module.
 */
@KStreamlinedExtensionMarker
public interface AndroidAppExtension {
    /**
     * Enable Compose.
     */
    public fun compose()

    /**
     * Enable Hilt by adding the `hilt-compiler` using KSP and adding the `hilt-android` runtime dependency.
     */
    public fun hilt()

    /**
     * Enable kotlinx.serialization by applying the `org.jetbrains.kotlin.plugin.serialization` plugin.
     */
    public fun serialization()

    /**
     * Configure app versioning.
     */
    public fun versioning(action: Action<AppVersioningExtension>)

    /**
     * Configure Google Play publishing.
     */
    public fun playPublishing(serviceAccountCredentials: File)

    /**
     * Enable and configure Google Services plugin.
     */
    public fun googleServices()

    /**
     * Enable baseline profile generation.
     */
    public fun consumeBaselineProfile(baselineProfileProjectPath: String)

    /**
     * Enable OSS licenses info generation using the Licensee plugin.
     */
    public fun generateLicensesInfo()

    /**
     * Enable unit tests.
     */
    public fun unitTests()

    /**
     * Configure dependencies.
     */
    public fun dependencies(action: Action<KSDependencies.Android.App>)
}

@Suppress("TooManyFunctions")
internal abstract class AndroidAppExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
    private val applicationId: String,
    private val baseApkName: String,
) : AndroidAppExtension, TopLevelExtension {
    private var composeEnabled: Boolean = false

    private var hiltEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var versioningConfig: Action<AppVersioningExtension>? = null

    private var playPublishingServiceAccountCredentials: File? = null

    private var googleServicesEnabled: Boolean = false

    private var baselineProfileProjectPath: String? = null

    private var licensesInfoGenerationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.App>? = null

    override fun compose() {
        composeEnabled = true
    }

    override fun hilt() {
        hiltEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun versioning(action: Action<AppVersioningExtension>) {
        versioningConfig = action
    }

    override fun playPublishing(serviceAccountCredentials: File) {
        playPublishingServiceAccountCredentials = serviceAccountCredentials
    }

    override fun googleServices() {
        googleServicesEnabled = true
    }

    override fun consumeBaselineProfile(baselineProfileProjectPath: String) {
        this.baselineProfileProjectPath = baselineProfileProjectPath
    }

    override fun generateLicensesInfo() {
        licensesInfoGenerationEnabled = true
    }

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun dependencies(action: Action<KSDependencies.Android.App>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        pluginManager.apply("com.android.application")

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureBuiltInKotlin(this, enableExplicitApi = false)
        }

        extensions.configure(ApplicationExtension::class.java) {
            it.configureAndroidApplicationExtension(
                project = project,
                namespace = namespace,
                applicationId = applicationId,
            )

            dependenciesBlock?.let { block ->
                configureDependencies(
                    sourceSets = it.sourceSets,
                    dependenciesBlock = block,
                )
            }
        }

        extensions.configure(BasePluginExtension::class.java) {
            it.archivesName.set(baseApkName)
        }

        extensions.configure(ApplicationAndroidComponentsExtension::class.java) {
            it.configureAndroidApplicationVariants()
        }

        versioningConfig?.let(::configureAppVersioning)

        playPublishingServiceAccountCredentials?.let(::configurePlayPublishing)

        if (googleServicesEnabled) {
            configureGoogleServices()
        }

        if (composeEnabled) {
            configureCompose(
                jvmTargetEnabled = false,
                androidTargetEnabled = true,
                iosTargetEnabled = false,
            )
        }

        if (hiltEnabled) {
            pluginManager.apply("com.google.dagger.hilt.android")
            configureKsp()
            with(dependencies) {
                add("ksp", libs.hilt.compiler)
                add("implementation", libs.hilt.android)
            }
        }

        if (serializationEnabled) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }

        baselineProfileProjectPath?.let(::configureBaselineProfile)

        if (licensesInfoGenerationEnabled) {
            configureLicensee()
        }

        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configurePowerAssert()
            configureTest()
        }

        configureDetekt()
    }

    private fun configureAppVersioning(action: Action<AppVersioningExtension>) = with(project) {
        pluginManager.apply("io.github.reactivecircus.app-versioning")
        extensions.configure(AppVersioningExtension::class.java) {
            action.execute(it)
        }
    }

    private fun configurePlayPublishing(serviceAccountCredentials: File) = with(project) {
        pluginManager.apply("com.github.triplet.play")
        extensions.configure(PlayPublisherExtension::class.java) {
            it.enabled.set(false) // only enable for prodRelease variant
            it.serviceAccountCredentials.set(serviceAccountCredentials)
            it.defaultToAppBundles.set(true)
        }
        extensions.configure(ApplicationExtension::class.java) { extension ->
            (extension as ExtensionAware).extensions
                .configure<NamedDomainObjectContainer<PlayPublisherExtension>>("playConfigs") { container ->
                    container.register(ProductFlavors.Prod) {
                        it.enabled.set(true)
                    }
                }
        }
    }

    private fun configureGoogleServices() = with(project) {
        pluginManager.apply("com.google.gms.google-services")
        if (isCiBuild) {
            extensions.configure(GoogleServicesPlugin.GoogleServicesPluginConfig::class.java) {
                it.missingGoogleServicesStrategy = GoogleServicesPlugin.MissingGoogleServicesStrategy.IGNORE
                tasks.withType(UploadDistributionTask::class.java).configureEach { task ->
                    if (name.endsWith("devDebug", ignoreCase = true)) {
                        task.dependsOn("processDevDebugGoogleServices")
                    }
                }
            }
        }
    }

    private fun configureBaselineProfile(projectPath: String) = with(project) {
        pluginManager.apply("androidx.baselineprofile")
        extensions.configure(BaselineProfileConsumerExtension::class.java) {
            it.mergeIntoMain = true
            with(it.warnings) {
                maxAgpVersion = false
                disabledVariants = false
            }
            dependencies.add("baselineProfile", project(projectPath))
        }
    }

    private fun configureLicensee() = with(project) {
        pluginManager.apply("app.cash.licensee")
        extensions.configure(LicenseeExtension::class.java) {
            it.bundleAndroidAsset.set(true)
            it.androidAssetReportPath.set("licensee/artifacts.json")
            it.allow("Apache-2.0")
            it.allow("MIT")
            it.allow("BSD-3-Clause")
            it.allowUrl("https://opensource.org/license/MIT")
            it.allowUrl("https://developer.android.com/studio/terms.html")
            it.unusedAction(UnusedAction.IGNORE)
        }
    }
}
