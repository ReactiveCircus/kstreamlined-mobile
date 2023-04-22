package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.buildlogic.applyLanguageSettings
import io.github.reactivecircus.kstreamlined.buildlogic.configureCommonAndroidOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlinJvm
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
            configureKotlinJvm(target)
            sourceSets.all {
                languageSettings {
                    applyLanguageSettings()
                }
            }
        }

        extensions.configure<BaseAppModuleExtension> {
            configureCommonAndroidOptions(target)

            lint {
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

        configureTest()
        configureDetekt()
        markNonCompatibleConfigurationCacheTasks()
        configureSlimTests()
    }
}
