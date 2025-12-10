@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import FlavorDimensions
import ProductFlavors
import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import app.cash.licensee.LicenseeExtension
import app.cash.licensee.UnusedAction
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.triplet.gradle.play.PlayPublisherExtension
import com.google.firebase.appdistribution.gradle.AppDistributionExtension
import com.google.firebase.appdistribution.gradle.tasks.UploadDistributionTask
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.firebase.perf.plugin.FirebasePerfExtension
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
@Suppress("TooManyFunctions")
@KStreamlinedExtensionMarker
public interface AndroidAppExtension {
    /**
     * Configure custom R8 keep rule.
     */
    public fun keepRule(keepRuleFile: File)

    /**
     * Configure app versioning.
     */
    public fun versioning(action: Action<AppVersioningExtension>)

    /**
     * Configure Google Play publishing.
     */
    public fun playPublishing(serviceAccountCredentials: File)

    /**
     * Enable and configure Firebase Performance Monitoring plugin.
     */
    public fun firebasePerf()

    /**
     * Enable and configure Firebase Crashlytics plugin.
     */
    public fun firebaseCrashlytics()

    /**
     * Enable and configure Firebase App Distribution plugin.
     */
    public fun firebaseAppDistribution(testerGroups: String, serviceAccountCredentials: File)

    /**
     * Enable baseline profile generation.
     */
    public fun consumeBaselineProfile(baselineProfileProjectPath: String)

    /**
     * Enable OSS licenses info generation using the Licensee plugin.
     */
    public fun generateLicensesInfo()

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
    private var keepRuleFile: File? = null

    private var versioningConfig: Action<AppVersioningExtension>? = null

    private var playPublishingServiceAccountCredentials: File? = null

    private var firebasePerfEnabled: Boolean = false

    private var firebaseCrashlyticsEnabled: Boolean = false

    private var firebaseAppDistributionEnabled: Boolean = false

    private var firebaseAppDistributionTesterGroups: String? = null

    private var firebaseAppDistributionServiceAccountCredentials: File? = null

    private val googleServicesEnabled: Boolean
        get() = firebasePerfEnabled || firebaseCrashlyticsEnabled || firebaseAppDistributionEnabled

    private var baselineProfileProjectPath: String? = null

    private var licensesInfoGenerationEnabled: Boolean = false

    private var composeEnabled: Boolean = false

    private var hiltEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.App>? = null

    override fun keepRule(keepRuleFile: File) {
        this.keepRuleFile = keepRuleFile
    }

    override fun versioning(action: Action<AppVersioningExtension>) {
        versioningConfig = action
    }

    override fun playPublishing(serviceAccountCredentials: File) {
        playPublishingServiceAccountCredentials = serviceAccountCredentials
    }

    override fun firebasePerf() {
        firebasePerfEnabled = true
    }

    override fun firebaseCrashlytics() {
        firebaseCrashlyticsEnabled = true
    }

    override fun firebaseAppDistribution(testerGroups: String, serviceAccountCredentials: File) {
        firebaseAppDistributionEnabled = true
        firebaseAppDistributionTesterGroups = testerGroups
        firebaseAppDistributionServiceAccountCredentials = serviceAccountCredentials
    }

    override fun consumeBaselineProfile(baselineProfileProjectPath: String) {
        this.baselineProfileProjectPath = baselineProfileProjectPath
    }

    override fun generateLicensesInfo() {
        licensesInfoGenerationEnabled = true
    }

    override fun compose() {
        composeEnabled = true
    }

    override fun hilt() {
        hiltEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun unitTests() {
        unitTestsEnabled = true
    }

    override fun dependencies(action: Action<KSDependencies.Android.App>) {
        dependenciesBlock = action
    }

    override fun evaluate() = with(project) {
        if (keepRuleFile == null) {
            error("Missing keepRule(keepRuleFile: File) configuration.")
        }

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

            it.configureBuildTypes(
                signingConfigs = it.signingConfigs,
                keepRules = listOf(it.getDefaultProguardFile("proguard-android-optimize.txt"), keepRuleFile!!),
            )

            it.configureProductFlavors()

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

        if (firebasePerfEnabled) {
            pluginManager.apply("com.google.firebase.firebase-perf")
        }

        if (firebaseCrashlyticsEnabled) {
            pluginManager.apply("com.google.firebase.crashlytics")
        }

        if (firebaseAppDistributionEnabled) {
            pluginManager.apply("com.google.firebase.appdistribution")
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

    private fun ApplicationExtension.configureBuildTypes(
        signingConfigs: NamedDomainObjectContainer<out ApkSigningConfig>,
        keepRules: List<File>,
    ) = buildTypes {
        with(getByName("debug")) {
            if (firebasePerfEnabled) {
                isDefault = true
                matchingFallbacks.add("release")
//                signingConfig = signingConfigs.getByName("debug")

                project.pluginManager.withPlugin("com.google.firebase.firebase-perf") {
                    // disable performance monitoring plugin for debug builds
                    (this as ExtensionAware).extensions.configure(FirebasePerfExtension::class.java) {
                        it.setInstrumentationEnabled(false)
                    }
                }
            }
        }
        with(getByName("release")) {
            matchingFallbacks.add("release")
//            signingConfig = if (rootDir.resolve("android/secrets/kstreamlined.jks").exists()) {
//                signingConfigs.getByName("release")
//            } else {
//                signingConfigs.getByName("debug")
//            }

            if (firebaseCrashlyticsEnabled) {
                project.pluginManager.withPlugin("com.google.firebase.crashlytics") {
                    // only upload mapping file on CI
                    (this as ExtensionAware).extensions.configure(CrashlyticsExtension::class.java) {
                        it.mappingFileUploadEnabled = project.isCiBuild
                    }
                }
            }

            @Suppress("UnstableApiUsage")
            optimization {
                enable = true
                keepRules {
                    files.addAll(keepRules)
                }
            }
        }
    }

    private fun ApplicationExtension.configureProductFlavors() {
        flavorDimensions.add(FlavorDimensions.Environment)
        productFlavors {
            register(ProductFlavors.Mock) {
                it.dimension = FlavorDimensions.Environment
                it.applicationIdSuffix = ".${ProductFlavors.Mock}"
            }
            register(ProductFlavors.Dev) { flavor ->
                flavor.isDefault = true
                flavor.dimension = FlavorDimensions.Environment
                flavor.applicationIdSuffix = ".${ProductFlavors.Dev}"

                if (firebaseAppDistributionEnabled) {
                    // distribute dev flavor for QA
                    project.pluginManager.withPlugin("com.google.firebase.appdistribution") {
                        // only upload mapping file on CI
                        (flavor as ExtensionAware).extensions.configure(AppDistributionExtension::class.java) {
                            it.groups = firebaseAppDistributionTesterGroups
                            it.serviceCredentialsFile = firebaseAppDistributionServiceAccountCredentials!!.absolutePath
                        }
                    }
                }
            }
            register(ProductFlavors.Demo) {
                it.dimension = FlavorDimensions.Environment
                it.applicationIdSuffix = ".${ProductFlavors.Demo}"
            }
            register(ProductFlavors.Prod) {
                it.dimension = FlavorDimensions.Environment
            }
        }
        // common source set for dev and prod
        sourceSets {
            named(ProductFlavors.Dev) {
                it.kotlin.directories.add("src/devAndProd/kotlin")
            }
            named(ProductFlavors.Prod) {
                it.kotlin.directories.add("src/devAndProd/kotlin")
            }
        }
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
        extensions.configure(GoogleServicesPlugin.GoogleServicesPluginConfig::class.java) {
            it.missingGoogleServicesStrategy = GoogleServicesPlugin.MissingGoogleServicesStrategy.IGNORE
        }
        if (isCiBuild) {
            tasks.withType(UploadDistributionTask::class.java).configureEach { task ->
                if (name.endsWith("devDebug", ignoreCase = true)) {
                    task.dependsOn("processDevDebugGoogleServices")
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
