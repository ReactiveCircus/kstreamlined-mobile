package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.buildlogic.configureCommonAndroidExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.buildlogic.configureSlimTests
import io.github.reactivecircus.kstreamlined.buildlogic.configureTest
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
            configureKotlin(target, enableExplicitApi = false)
        }

        extensions.configure<BaseAppModuleExtension> {
            configureCommonAndroidExtension(target)

            lint {
                quiet = false
                ignoreWarnings = false
                htmlReport = true
                xmlReport = true
                htmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.html")
                xmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.xml")
                checkDependencies = true
                ignoreTestSources = true
            }
        }

        extensions.configure<ApplicationAndroidComponentsExtension> {
            configureAndroidApplicationVariants()
        }

        configureTest()
        configureSlimTests()
        configureDetekt()
    }
}
