package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.buildlogic.applyLanguageSettings
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.buildlogic.configureCommonAndroidOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlinJvmCompileOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureSlimTests
import io.github.reactivecircus.kstreamlined.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.buildlogic.markNonCompatibleConfigurationCacheTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            sourceSets.all {
                languageSettings {
                    applyLanguageSettings()
                }
            }
        }

        extensions.configure<BaseAppModuleExtension> {
            configureCommonAndroidOptions(target)

            @Suppress("UnstableApiUsage")
            lint {
                disable.add("ParcelCreator")
                disable.add("GoogleAppIndexingWarning")
                disable.add("DialogFragmentCallbacksDetector") // TODO remove once fixed: https://issuetracker.google.com/issues/187524311
                disable.add("NotificationPermission") // TODO remove once LeakCanary supports Android 13: https://github.com/square/leakcanary/issues/2398
                quiet = false
                ignoreWarnings = false
                htmlReport = true
                xmlReport = true
                htmlOutput = File("${project.buildDir}/reports/lint/lint-reports.html")
                xmlOutput = File("${project.buildDir}/reports/lint/lint-reports.xml")
                checkDependencies = true
                ignoreTestSources = true
            }
        }

        extensions.configure<ApplicationAndroidComponentsExtension> {
            configureAndroidApplicationVariants(target)
        }

        configureKotlinJvmCompileOptions()
        configureTest()
        configureDetekt()
        markNonCompatibleConfigurationCacheTasks()
        configureSlimTests()
    }
}
