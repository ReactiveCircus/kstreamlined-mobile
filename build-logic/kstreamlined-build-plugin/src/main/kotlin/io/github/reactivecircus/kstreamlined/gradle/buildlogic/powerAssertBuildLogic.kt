package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

/**
 * Apply and configure Power-assert plugin.
 */
internal fun Project.configurePowerAssert() {
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
}
