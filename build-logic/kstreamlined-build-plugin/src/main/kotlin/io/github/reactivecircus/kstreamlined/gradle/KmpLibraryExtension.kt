@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import app.cash.sqldelight.gradle.SqlDelightDatabase
import app.cash.sqldelight.gradle.SqlDelightExtension
import com.apollographql.apollo.gradle.api.ApolloExtension
import com.apollographql.apollo.gradle.api.Service
import io.github.reactivecircus.kstreamlined.gradle.internal.KmpTargetsConfig
import io.github.reactivecircus.kstreamlined.gradle.internal.configureCompose
import io.github.reactivecircus.kstreamlined.gradle.internal.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKmpTargets
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKmpTest
import io.github.reactivecircus.kstreamlined.gradle.internal.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.internal.configurePowerAssert
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinDependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import javax.inject.Inject

/**
 * Entry point for configuring a KMP library module.
 */
@Suppress("TooManyFunctions")
@KStreamlinedExtensionMarker
public interface KmpLibraryExtension {
    /**
     * Configure targets.
     */
    public fun targets(action: Action<TargetsOptions>)

    /**
     * Enable Compose.
     */
    public fun compose()

    /**
     * Enable kotlinx.serialization by applying the `org.jetbrains.kotlin.plugin.serialization` plugin.
     */
    public fun serialization()

    /**
     * Create a new Apollo service with the provided [name].
     */
    public fun apolloService(name: String, action: Action<Service>)

    /**
     * Create a new SQLDelight database with the provided [name].
     */
    public fun sqlDelightDatabase(name: String, action: Action<SqlDelightDatabase>)

    /**
     * Enable unit tests.
     */
    public fun unitTests(androidHostTests: Boolean = false)

    /**
     * Configure common dependencies.
     */
    public fun dependencies(action: Action<KotlinDependencies>)

    /**
     * Configure `commonMain` dependencies.
     */
    public fun commonMainDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `commonTest` dependencies.
     */
    public fun commonTestDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `jvmMain` dependencies.
     */
    public fun jvmMainDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `jvmTest` dependencies.
     */
    public fun jvmTestDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `androidMain` dependencies.
     */
    public fun androidMainDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `androidUnitTest` dependencies.
     */
    public fun androidUnitTestDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `iosMain` dependencies.
     */
    public fun iosMainDependencies(action: Action<KotlinDependencyHandler>)

    /**
     * Configure `iosTest` dependencies.
     */
    public fun iosTestDependencies(action: Action<KotlinDependencyHandler>)

    @KStreamlinedExtensionMarker
    public interface TargetsOptions {
        /**
         * Enable JVM target.
         */
        public fun jvm()

        /**
         * Enable Android target.
         */
        public fun android(namespace: String)

        /**
         * Enable iOS targets.
         */
        public fun ios()
    }
}

@Suppress("TooManyFunctions")
internal abstract class KmpLibraryExtensionImpl @Inject constructor(
    private val project: Project,
) : KmpLibraryExtension, TopLevelExtension {
    private val targetsOptions = project.objects.newInstance(TargetsOptionsImpl::class.java)

    private var composeEnabled: Boolean = false

    private var serializationEnabled: Boolean = false

    private val apolloServiceConfigs: MutableMap<String, Action<Service>> = mutableMapOf()

    private val sqlDelightDatabaseConfigs: MutableMap<String, Action<SqlDelightDatabase>> = mutableMapOf()

    private var unitTestsEnabled: Boolean = false

    private var androidHostTestsEnabled: Boolean = false

    private var dependenciesBlock: Action<KotlinDependencies>? = null

    private var commonMainDependenciesBlock: Action<KotlinDependencyHandler>? = null
    private var commonTestDependenciesBlock: Action<KotlinDependencyHandler>? = null

    private var jvmMainDependenciesBlock: Action<KotlinDependencyHandler>? = null
    private var jvmTestDependenciesBlock: Action<KotlinDependencyHandler>? = null

    private var androidMainDependenciesBlock: Action<KotlinDependencyHandler>? = null
    private var androidUnitTestDependenciesBlock: Action<KotlinDependencyHandler>? = null

    private var iosMainDependenciesBlock: Action<KotlinDependencyHandler>? = null
    private var iosTestDependenciesBlock: Action<KotlinDependencyHandler>? = null

    override fun targets(action: Action<KmpLibraryExtension.TargetsOptions>) {
        action.execute(targetsOptions)
    }

    override fun compose() {
        composeEnabled = true
    }

    override fun serialization() {
        serializationEnabled = true
    }

    override fun apolloService(name: String, action: Action<Service>) {
        apolloServiceConfigs[name] = action
    }

    override fun sqlDelightDatabase(name: String, action: Action<SqlDelightDatabase>) {
        sqlDelightDatabaseConfigs[name] = action
    }

    override fun unitTests(androidHostTests: Boolean) {
        unitTestsEnabled = true
        androidHostTestsEnabled = androidHostTests
    }

    override fun dependencies(action: Action<KotlinDependencies>) {
        dependenciesBlock = action
    }

    override fun commonMainDependencies(action: Action<KotlinDependencyHandler>) {
        commonMainDependenciesBlock = action
    }

    override fun commonTestDependencies(action: Action<KotlinDependencyHandler>) {
        commonTestDependenciesBlock = action
    }

    override fun jvmMainDependencies(action: Action<KotlinDependencyHandler>) {
        jvmMainDependenciesBlock = action
    }

    override fun jvmTestDependencies(action: Action<KotlinDependencyHandler>) {
        jvmTestDependenciesBlock = action
    }

    override fun androidMainDependencies(action: Action<KotlinDependencyHandler>) {
        androidMainDependenciesBlock = action
    }

    override fun androidUnitTestDependencies(action: Action<KotlinDependencyHandler>) {
        androidUnitTestDependenciesBlock = action
    }

    override fun iosMainDependencies(action: Action<KotlinDependencyHandler>) {
        iosMainDependenciesBlock = action
    }

    override fun iosTestDependencies(action: Action<KotlinDependencyHandler>) {
        iosTestDependenciesBlock = action
    }

    override fun evaluate() = with(targetsOptions) {
        if (!jvmEnabled && !androidEnabled && !iosEnabled) {
            error(
                """
                |At least one of the targets must be enabled:
                |- jvm()
                |- android(namespace: String)
                |- ios()
                """.trimMargin(),
            )
        }
        if (this@KmpLibraryExtensionImpl.androidHostTestsEnabled && !androidEnabled) {
            error("`androidHostTests` can only be enabled when `android` target is enabled.")
        }

        with(this@KmpLibraryExtensionImpl.project) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            if (jvmEnabled || androidEnabled) {
                pluginManager.apply("com.android.lint")
            }
            if (androidEnabled) {
                pluginManager.apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) { extension ->
                extension.configureKmpTargets(
                    project = this,
                    config = KmpTargetsConfig(
                        jvmTargetEnabled = jvmEnabled,
                        androidTargetEnabled = androidEnabled,
                        iosTargetEnabled = iosEnabled,
                        androidNamespace = androidNamespace,
                        androidHostTestsEnabled = this@KmpLibraryExtensionImpl.androidHostTestsEnabled,
                    ),
                )
                extension.configureKotlin(this)

                this@KmpLibraryExtensionImpl.configureDependencies(extension)
            }

            if (this@KmpLibraryExtensionImpl.composeEnabled) {
                configureCompose(
                    jvmTargetEnabled = jvmEnabled,
                    androidTargetEnabled = androidEnabled,
                    iosTargetEnabled = iosEnabled,
                )
            }

            if (this@KmpLibraryExtensionImpl.serializationEnabled) {
                pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            }

            this@KmpLibraryExtensionImpl.configureApolloServices()

            this@KmpLibraryExtensionImpl.configureSqlDelightDatabases()

            if (this@KmpLibraryExtensionImpl.unitTestsEnabled) {
                configurePowerAssert()
                extensions.configure(KotlinMultiplatformExtension::class.java) {
                    it.configureKmpTest(project)
                }
            }

            configureDetekt()
        }
    }

    private fun configureDependencies(extension: KotlinMultiplatformExtension) = with(extension) {
        this@KmpLibraryExtensionImpl.dependenciesBlock?.let { dependencies(it) }
        with(sourceSets) {
            commonMainDependenciesBlock?.let {
                commonMain { dependencies(it) }
            }
            commonTestDependenciesBlock?.let {
                commonTest { dependencies(it) }
            }
            jvmMainDependenciesBlock?.let {
                jvmMain { dependencies(it) }
            }
            jvmTestDependenciesBlock?.let {
                jvmTest { dependencies(it) }
            }
            androidMainDependenciesBlock?.let {
                androidMain { dependencies(it) }
            }
            androidMainDependenciesBlock?.let {
                androidUnitTest { dependencies(it) }
            }
            iosMainDependenciesBlock?.let {
                iosMain { dependencies(it) }
            }
            iosTestDependenciesBlock?.let {
                iosTest { dependencies(it) }
            }
        }
    }

    private fun configureApolloServices() = with(project) {
        val serviceConfigs = apolloServiceConfigs
        if (serviceConfigs.isNotEmpty()) {
            pluginManager.apply("com.apollographql.apollo")
            extensions.configure(ApolloExtension::class.java) {
                serviceConfigs.forEach { (name, action) ->
                    it.service(name) { service ->
                        action.execute(service)
                    }
                }
            }
        }
    }

    private fun configureSqlDelightDatabases() = with(project) {
        val dbConfigs = sqlDelightDatabaseConfigs
        if (dbConfigs.isNotEmpty()) {
            pluginManager.apply("app.cash.sqldelight")
            extensions.configure(SqlDelightExtension::class.java) {
                dbConfigs.forEach { (name, action) ->
                    it.databases.create(name) { database ->
                        action.execute(database)
                    }
                }
            }
        }
    }

    internal abstract class TargetsOptionsImpl : KmpLibraryExtension.TargetsOptions {
        var jvmEnabled: Boolean = false

        var androidEnabled: Boolean = false

        var androidNamespace: String? = null

        var iosEnabled: Boolean = false

        override fun jvm() {
            jvmEnabled = true
        }

        override fun android(namespace: String) {
            androidEnabled = true
            androidNamespace = namespace
        }

        override fun ios() {
            iosEnabled = true
        }
    }
}
