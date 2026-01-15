@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import FlavorDimensions
import ProductFlavors
import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import com.android.build.api.variant.Variant
import com.google.firebase.appdistribution.gradle.AppDistributionExtension
import com.google.firebase.appdistribution.gradle.tasks.UploadDistributionTask
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.firebase.perf.plugin.FirebasePerfExtension
import com.google.gms.googleservices.GoogleServicesPlugin
import io.github.reactivecircus.aabpublisher.AabPublisherExtension
import io.github.reactivecircus.appversioning.AppVersioningExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidApplicationExtension
import io.github.reactivecircus.kstreamlined.gradle.internal.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.gradle.internal.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.internal.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.internal.configureLicensesInfoGeneration
import io.github.reactivecircus.kstreamlined.gradle.internal.configureMetro
import io.github.reactivecircus.kstreamlined.gradle.internal.configurePowerAssert
import io.github.reactivecircus.kstreamlined.gradle.internal.configureTest
import io.github.reactivecircus.kstreamlined.gradle.internal.libs
import isCiBuild
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import java.io.File
import java.io.Serializable
import javax.inject.Inject

/**
 * Entry point for configuring an Android application module.
 */
@Suppress("TooManyFunctions")
@KStreamlinedExtensionMarker
public interface AndroidAppExtension {
    /**
     * Enable and configure build config fields generation.
     */
    public fun buildConfigs(callback: BuildConfigsOptions.(Variant) -> Unit)

    /**
     * Enable and configure res values generation.
     */
    public fun resValues(callback: ResValuesOptions.(Variant) -> Unit)

    /**
     * Configure app signing configs.
     */
    public fun signing(action: Action<AppSigningOptions>)

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
     * Enable Metro.
     */
    public fun metro()

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

    @KStreamlinedExtensionMarker
    public interface BuildConfigsOptions {
        public fun <T : Serializable> buildConfigField(key: String, value: T)

        public fun <T : Serializable> buildConfigField(key: String, value: Provider<T>)
    }

    @KStreamlinedExtensionMarker
    public interface ResValuesOptions {
        public fun resValueFromString(key: String, value: String)

        public fun resValueFromBoolean(key: String, value: Boolean)
    }

    @KStreamlinedExtensionMarker
    public interface AppSigningOptions {
        /**
         * Configure signing configs for the Debug build type.
         */
        public fun debug(action: Action<PerBuildTypeOptions>)

        /**
         * Configure signing configs for the Release build type.
         */
        public fun release(action: Action<PerBuildTypeOptions>)

        @KStreamlinedExtensionMarker
        public interface PerBuildTypeOptions {
            /**
             * Configure store file used when signing.
             */
            public fun storeFile(storeFile: File)

            /**
             * Configure store password used when signing.
             */
            public fun storePassword(storePassword: String)

            /**
             * Configure key alias used when signing.
             */
            public fun keyAlias(keyAlias: String)

            /**
             * Configure key password used when signing.
             */
            public fun keyPassword(keyPassword: String)
        }
    }
}

@Suppress("TooManyFunctions")
internal abstract class AndroidAppExtensionImpl @Inject constructor(
    private val project: Project,
    private val namespace: String,
    private val applicationId: String,
    private val baseApkName: String,
) : AndroidAppExtension, TopLevelExtension {
    private val buildConfigsOptionsForVariants = mutableMapOf<String, BuildConfigsOptionsImpl>()

    private val resValuesOptionsForVariants = mutableMapOf<String, ResValuesOptionsImpl>()

    private var buildConfigsCallback: (AndroidAppExtension.BuildConfigsOptions.(Variant) -> Unit)? = null

    private var resValuesCallback: (AndroidAppExtension.ResValuesOptions.(Variant) -> Unit)? = null

    private val appSigningOptions = project.objects.newInstance(AppSigningOptionsImpl::class.java)

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

    private var metroEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private var unitTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KSDependencies.Android.App>? = null

    override fun buildConfigs(callback: AndroidAppExtension.BuildConfigsOptions.(Variant) -> Unit) {
        buildConfigsCallback = callback
    }

    override fun resValues(callback: AndroidAppExtension.ResValuesOptions.(Variant) -> Unit) {
        resValuesCallback = callback
    }

    override fun signing(action: Action<AndroidAppExtension.AppSigningOptions>) {
        action.execute(appSigningOptions)
    }

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

    override fun metro() {
        metroEnabled = true
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

    @Suppress("CyclomaticComplexMethod")
    override fun evaluate() = with(project) {
        if (!appSigningOptions.fullyConfigured) {
            error(
                """
                |Missing signing configuration. The following must be configured for both Debug and Release build types:
                |- storeFile(storeFile: File)
                |- storePassword(storePassword: String)
                |- keyAlias(keyAlias: String)
                |- keyPassword(keyPassword: String)
                """.trimMargin(),
            )
        }

        if (keepRuleFile == null) {
            error("Missing keepRule(keepRuleFile: File) configuration.")
        }

        pluginManager.apply("com.android.application")

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureKotlin(this, enableExplicitApi = false)
        }

        extensions.configure(ApplicationExtension::class.java) {
            it.configureAndroidApplicationExtension(
                project = project,
                namespace = namespace,
                applicationId = applicationId,
            )

            it.configureSigningConfigs()

            it.configureBuildTypes(
                keepRules = listOf(it.getDefaultProguardFile("proguard-android-optimize.txt"), keepRuleFile!!),
            )

            it.configureProductFlavors()

            it.buildFeatures {
                buildConfig = buildConfigsCallback != null
                resValues = resValuesCallback != null
            }

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

        extensions.configure(ApplicationAndroidComponentsExtension::class.java) { extension ->
            extension.configureAndroidApplicationVariants(project, unitTestsEnabled)

            extension.onVariants { variant ->
                buildConfigsCallback?.let { callback ->
                    buildConfigsOptionsForVariants.getOrPut(variant.name) {
                        project.objects.newInstance(BuildConfigsOptionsImpl::class.java, variant)
                    }.callback(variant)
                }
                resValuesCallback?.let { callback ->
                    resValuesOptionsForVariants.getOrPut(variant.name) {
                        project.objects.newInstance(ResValuesOptionsImpl::class.java, variant)
                    }.callback(variant)
                }
            }
        }

        versioningConfig?.let(::configureAppVersioning)

        playPublishingServiceAccountCredentials?.let(::configureAabPublisher)

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

        if (metroEnabled) {
            configureMetro()
        }

        if (serializationEnabled) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }

        baselineProfileProjectPath?.let(::configureBaselineProfile)

        if (licensesInfoGenerationEnabled) {
            configureLicensesInfoGeneration(variantForCodegenOnSync = "devDebug")
        }

        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configurePowerAssert()
            configureTest()
        }

        configureDetekt()
    }

    private fun ApplicationExtension.configureSigningConfigs() = signingConfigs {
        fun ApkSigningConfig.setFrom(options: AppSigningOptionsImpl.PerBuildTypeOptionsImpl) {
            storeFile = options.storeFile
            storePassword = options.storePassword
            keyAlias = options.keyAlias
            keyPassword = options.keyPassword
        }
        named("debug") {
            it.setFrom(appSigningOptions.debugSigningOptions)
        }
        register("release") {
            // fallbacks to debug signing config if store file configured for release build doesn't exist
            it.setFrom(
                options = if (appSigningOptions.releaseSigningOptions.storeFile?.exists() == true) {
                    appSigningOptions.releaseSigningOptions
                } else {
                    appSigningOptions.debugSigningOptions
                },
            )
        }
    }

    private fun ApplicationExtension.configureBuildTypes(keepRules: List<File>) = buildTypes {
        with(getByName("debug")) {
            if (firebasePerfEnabled) {
                isDefault = true
                matchingFallbacks.add("release")
                signingConfig = signingConfigs.getByName("debug")

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
            signingConfig = signingConfigs.getByName("release")

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

    private fun configureAabPublisher(serviceAccountCredentials: File) = with(project) {
        pluginManager.apply("io.github.reactivecircus.aab-publisher")
        extensions.configure(AabPublisherExtension::class.java) {
            it.variant.set("prodRelease") // publish prodRelease variant
            it.serviceAccountCredentials.set(serviceAccountCredentials)
        }
    }

    private fun configureGoogleServices() = with(project) {
        pluginManager.apply("com.google.gms.google-services")
        extensions.configure(GoogleServicesPlugin.GoogleServicesPluginConfig::class.java) {
            it.missingGoogleServicesStrategy = GoogleServicesPlugin.MissingGoogleServicesStrategy.IGNORE
        }
        if (isCiBuild) {
            tasks.withType(UploadDistributionTask::class.java).configureEach { task ->
                if (task.name.endsWith("devDebug", ignoreCase = true)) {
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

    internal abstract class AppSigningOptionsImpl @Inject constructor(
        objects: ObjectFactory,
    ) : AndroidAppExtension.AppSigningOptions {
        val debugSigningOptions = objects.newInstance(PerBuildTypeOptionsImpl::class.java)

        val releaseSigningOptions = objects.newInstance(PerBuildTypeOptionsImpl::class.java)

        override fun debug(action: Action<AndroidAppExtension.AppSigningOptions.PerBuildTypeOptions>) {
            action.execute(debugSigningOptions)
        }

        override fun release(action: Action<AndroidAppExtension.AppSigningOptions.PerBuildTypeOptions>) {
            action.execute(releaseSigningOptions)
        }

        val fullyConfigured: Boolean
            get() = debugSigningOptions.fullyConfigured && releaseSigningOptions.fullyConfigured

        abstract class PerBuildTypeOptionsImpl : AndroidAppExtension.AppSigningOptions.PerBuildTypeOptions {
            var storeFile: File? = null
                private set

            var storePassword: String? = null
                private set

            var keyAlias: String? = null
                private set

            var keyPassword: String? = null
                private set

            override fun storeFile(storeFile: File) {
                this.storeFile = storeFile
            }

            override fun storePassword(storePassword: String) {
                this.storePassword = storePassword
            }

            override fun keyAlias(keyAlias: String) {
                this.keyAlias = keyAlias
            }

            override fun keyPassword(keyPassword: String) {
                this.keyPassword = keyPassword
            }

            val fullyConfigured: Boolean
                get() = storeFile != null && storePassword != null && keyAlias != null && keyPassword != null
        }
    }

    internal abstract class BuildConfigsOptionsImpl @Inject constructor(
        private val variant: Variant,
    ) : AndroidAppExtension.BuildConfigsOptions {
        override fun <T : Serializable> buildConfigField(key: String, value: T) {
            variant.buildConfigFields?.put(
                key,
                if (value is String) {
                    BuildConfigField(type = value::class.java.simpleName, value = "\"$value\"", comment = null)
                } else {
                    BuildConfigField(type = value::class.java.simpleName, value = value, comment = null)
                },
            )
        }

        override fun <T : Serializable> buildConfigField(key: String, value: Provider<T>) {
            variant.buildConfigFields?.put(
                key,
                value.map {
                    if (it is String) {
                        BuildConfigField(type = it::class.java.simpleName, value = "\"$it\"", comment = null)
                    } else {
                        BuildConfigField(type = it::class.java.simpleName, value = it, comment = null)
                    }
                },
            )
        }
    }

    internal abstract class ResValuesOptionsImpl @Inject constructor(
        private val variant: Variant,
    ) : AndroidAppExtension.ResValuesOptions {
        override fun resValueFromString(key: String, value: String) = with(variant) {
            resValues.put(makeResValueKey(type = "string", key), ResValue(value))
        }

        override fun resValueFromBoolean(key: String, value: Boolean) = with(variant) {
            resValues.put(makeResValueKey(type = "bool", key), ResValue(value.toString()))
        }
    }
}
