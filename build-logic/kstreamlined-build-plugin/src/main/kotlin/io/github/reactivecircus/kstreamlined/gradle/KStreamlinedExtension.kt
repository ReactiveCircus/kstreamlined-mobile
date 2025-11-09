package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.KmpTargetsConfig
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKmpTargets
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKmpTest
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import javax.inject.Inject

/**
 * Entry point for all build configurations in KStreamlined.
 */
public interface KStreamlinedExtension {
    public fun kmpLibrary(action: Action<KmpLibraryExtension>)
}

internal abstract class KStreamlinedExtensionImpl @Inject constructor(objects: ObjectFactory) : KStreamlinedExtension {
    private val kmpLibraryExtension = objects.newInstance(KmpLibraryExtensionImpl::class.java)

    private var kmpLibraryConfigured: Boolean = false

    override fun kmpLibrary(action: Action<KmpLibraryExtension>) {
        action.execute(kmpLibraryExtension)
        kmpLibraryExtension.evaluate()
        kmpLibraryConfigured = true
    }

    internal fun validate() {
        if (!kmpLibraryConfigured) {
            error(
                """
                | One of the following top-level configuration blocks must be present in `kstreamlined {...}`:
                | - kmpLibrary {...}
                """.trimMargin(),
            )
        }
    }
}

/**
 * Entry point for configuring a KMP library module.
 */
public interface KmpLibraryExtension {
    /**
     * Configure targets.
     */
    public fun targets(action: Action<TargetsOptions>)

    /**
     * Enable unit tests.
     */
    public fun unitTests(androidHostTests: Boolean = false)

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

internal abstract class KmpLibraryExtensionImpl @Inject constructor(
    objects: ObjectFactory,
    private val project: Project,
) : KmpLibraryExtension {
    init {
        project.pluginManager.apply("org.jetbrains.kotlin.multiplatform")
    }

    private val targetsOptions = objects.newInstance(TargetsOptionsImpl::class.java)

    private var unitTestsEnabled: Boolean = false

    private var androidHostTestsEnabled: Boolean = false

    override fun targets(action: Action<KmpLibraryExtension.TargetsOptions>) {
        action.execute(targetsOptions)
    }

    override fun unitTests(androidHostTests: Boolean) {
        unitTestsEnabled = true
        androidHostTestsEnabled = androidHostTests
    }

    internal fun evaluate() = with(targetsOptions) {
        if (!jvmEnabled && !androidEnabled && !iosEnabled) {
            error(
                """
                | At least one of the targets must be enabled:
                | - jvm()
                | - android(namespace: String)
                | - ios()
                """.trimMargin(),
            )
        }
        if (androidHostTestsEnabled && !androidEnabled) {
            error("`androidHostTests` can only be enabled when `android` target is enabled.")
        }

        with(project) {
            if (jvmEnabled || androidEnabled) {
                pluginManager.apply("com.android.lint")
            }
            if (androidEnabled) {
                pluginManager.apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) {
                it.configureKmpTargets(
                    project = this,
                    config = KmpTargetsConfig(
                        jvmTargetEnabled = jvmEnabled,
                        androidTargetEnabled = androidEnabled,
                        iosTargetEnabled = iosEnabled,
                        androidNamespace = androidNamespace,
                        androidHostTestsEnabled = androidHostTestsEnabled,
                    ),
                )
                it.configureKotlin(this)
            }

            if (unitTestsEnabled) {
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
