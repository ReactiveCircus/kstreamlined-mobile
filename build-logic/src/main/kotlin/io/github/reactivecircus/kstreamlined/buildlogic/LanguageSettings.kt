package io.github.reactivecircus.kstreamlined.buildlogic

import org.jetbrains.kotlin.gradle.plugin.LanguageSettingsBuilder

internal fun LanguageSettingsBuilder.applyLanguageSettings(): LanguageSettingsBuilder {
    return apply {
        languageVersion = "1.7"
        progressiveMode = true
        optIn("kotlin.RequiresOptIn")
        optIn("kotlin.Experimental")
    }
}
