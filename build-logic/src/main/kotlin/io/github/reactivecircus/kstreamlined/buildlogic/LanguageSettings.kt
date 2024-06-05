package io.github.reactivecircus.kstreamlined.buildlogic

import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

internal fun LanguageSettingsBuilder.applyLanguageSettings(): LanguageSettingsBuilder {
    return apply {
        progressiveMode = true
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.experimental.ExperimentalObjCName")
        enableLanguageFeature("ContextReceivers")
    }
}
