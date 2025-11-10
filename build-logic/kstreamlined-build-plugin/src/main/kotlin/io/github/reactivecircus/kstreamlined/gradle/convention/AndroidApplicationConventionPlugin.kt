package io.github.reactivecircus.kstreamlined.gradle.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidApplicationVariants
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCommonAndroidExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
        }

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureBuiltInKotlin(target, enableExplicitApi = false)
        }

        extensions.configure(ApplicationExtension::class.java) {
            it.configureCommonAndroidExtension(target)
            it.configureAndroidApplicationExtension()
        }

        extensions.configure(ApplicationAndroidComponentsExtension::class.java) {
            it.configureAndroidApplicationVariants()
        }

        configureTest()
        configureDetekt()
    }
}
