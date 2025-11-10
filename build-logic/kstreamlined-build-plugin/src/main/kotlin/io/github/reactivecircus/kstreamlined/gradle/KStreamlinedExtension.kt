@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.KmpTargetsConfig
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKmpTargets
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKmpTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.libs
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinDependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import javax.inject.Inject

@DslMarker
internal annotation class KStreamlinedExtensionMarker

/**
 * Entry point for all build configurations in KStreamlined.
 */
@KStreamlinedExtensionMarker
public interface KStreamlinedExtension {
    public fun kmpLibrary(action: Action<KmpLibraryExtension>)

    public fun jvmLibrary(action: Action<JvmLibraryExtension> = Action {})
}

internal abstract class KStreamlinedExtensionImpl @Inject constructor(objects: ObjectFactory) : KStreamlinedExtension {
    private val kmpLibraryExtension = objects.newInstance(KmpLibraryExtensionImpl::class.java)
    private val jvmLibraryExtension = objects.newInstance(JvmLibraryExtensionImpl::class.java)

    private var kmpLibraryConfigured: Boolean = false
    private var jvmLibraryConfigured: Boolean = false

    override fun kmpLibrary(action: Action<KmpLibraryExtension>) {
        action.execute(kmpLibraryExtension)
        kmpLibraryExtension.evaluate()
        kmpLibraryConfigured = true
    }

    override fun jvmLibrary(action: Action<JvmLibraryExtension>) {
        action.execute(jvmLibraryExtension)
        jvmLibraryExtension.evaluate()
        jvmLibraryConfigured = true
    }

    internal fun validate() {
        if (!kmpLibraryConfigured && !jvmLibraryConfigured) {
            error(
                """
                |One of the following top-level configuration blocks must be present in `kstreamlined {...}`:
                |- kmpLibrary {...}
                |- jvmLibrary {...}
                """.trimMargin(),
            )
        }
    }
}

/**
 * Entry point for configuring a KMP library module.
 */
@KStreamlinedExtensionMarker
public interface KmpLibraryExtension {
    /**
     * Configure targets.
     */
    public fun targets(action: Action<TargetsOptions>)

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
    objects: ObjectFactory,
    private val project: Project,
) : KmpLibraryExtension {
    private val targetsOptions = objects.newInstance(TargetsOptionsImpl::class.java)

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

    internal fun evaluate() = with(targetsOptions) {
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

            if (this@KmpLibraryExtensionImpl.unitTestsEnabled) {
                pluginManager.apply("org.jetbrains.kotlin.plugin.power-assert")
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                extensions.configure(PowerAssertGradleExtension::class.java) {
                    it.functions.set(
                        listOf(
                            "kotlin.assert",
                            "kotlin.test.assertEquals",
                            "kotlin.test.assertTrue",
                            "kotlin.test.assertFalse",
                            "kotlin.test.assertNull",
                        ),
                    )
                }
                extensions.configure(KotlinMultiplatformExtension::class.java) {
                    it.configureKmpTest(project)
                }
            }

            configureDetekt()
        }
    }

    private fun configureDependencies(extension: KotlinMultiplatformExtension) = with(extension) {
        this@KmpLibraryExtensionImpl.dependenciesBlock?.run { dependencies(this) }
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

    internal abstract class TargetsOptionsImpl @Inject constructor(
        private val project: Project,
    ) : KmpLibraryExtension.TargetsOptions {
        internal var jvmEnabled: Boolean = false

        internal var androidEnabled: Boolean = false

        internal var androidNamespace: String? = null

        internal var iosEnabled: Boolean = false

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

/**
 * Entry point for configuring a JVM library module.
 */
@KStreamlinedExtensionMarker
public interface JvmLibraryExtension {
    /**
     * Enable unit tests.
     */
    public fun unitTests()
}

internal abstract class JvmLibraryExtensionImpl @Inject constructor(
    private val project: Project,
) : JvmLibraryExtension {
    private var unitTestsEnabled: Boolean = false

    override fun unitTests() {
        unitTestsEnabled = true
    }

    internal fun evaluate() = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("com.android.lint")

        extensions.configure(KotlinJvmProjectExtension::class.java) {
            it.configureKotlin(this)
        }

        if (unitTestsEnabled) {
            dependencies.add("testImplementation", libs.kotlin.test.junit)
            configureTest()
        }

        configureDetekt()
    }
}
