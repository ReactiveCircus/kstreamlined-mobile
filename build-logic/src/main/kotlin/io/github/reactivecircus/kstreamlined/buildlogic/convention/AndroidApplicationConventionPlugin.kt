package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidApplicationExtension
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
            configureCommonAndroidExtension()
            configureAndroidApplicationExtension(target)
        }

        extensions.configure<ApplicationAndroidComponentsExtension> {
            configureAndroidApplicationVariants()
        }

        configureTest()
        configureSlimTests()
        configureDetekt()
    }
}
