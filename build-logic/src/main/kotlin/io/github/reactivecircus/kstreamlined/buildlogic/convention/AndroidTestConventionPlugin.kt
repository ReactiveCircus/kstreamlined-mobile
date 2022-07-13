package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.TestAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import io.github.reactivecircus.kstreamlined.buildlogic.applyLanguageSettings
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidTestOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidTestVariants
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlinJvmCompileOptions
import io.github.reactivecircus.kstreamlined.buildlogic.markNonCompatibleConfigurationCacheTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.test")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            sourceSets.all {
                languageSettings {
                    applyLanguageSettings()
                }
            }
        }

        extensions.configure<TestExtension> {
            configureAndroidTestOptions()
        }

        extensions.configure<TestAndroidComponentsExtension> {
            configureAndroidTestVariants(target)
        }

        configureKotlinJvmCompileOptions()
        configureDetekt()
        markNonCompatibleConfigurationCacheTasks()
    }
}
