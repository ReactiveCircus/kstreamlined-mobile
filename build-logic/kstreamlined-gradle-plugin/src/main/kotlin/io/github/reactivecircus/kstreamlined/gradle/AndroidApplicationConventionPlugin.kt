package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCommonAndroidExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
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
        configureDetekt()
    }
}
