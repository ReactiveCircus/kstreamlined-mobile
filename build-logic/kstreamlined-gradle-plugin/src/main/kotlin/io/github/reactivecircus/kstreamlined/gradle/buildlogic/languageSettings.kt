package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

internal fun LanguageSettingsBuilder.applyLanguageSettings(): LanguageSettingsBuilder {
    return apply {
        progressiveMode = true
        optIn("kotlin.experimental.ExperimentalObjCName")
        enableLanguageFeature("ContextParameters")
        enableLanguageFeature("ExplicitBackingFields")
    }
}
