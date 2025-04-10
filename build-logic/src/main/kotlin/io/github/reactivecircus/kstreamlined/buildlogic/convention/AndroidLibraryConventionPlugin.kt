package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.buildlogic.configureCommonAndroidExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.buildlogic.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            configureKotlin(target)
        }

        extensions.configure<LibraryExtension> {
            configureCommonAndroidExtension()
        }

        extensions.configure<LibraryAndroidComponentsExtension> {
            configureAndroidLibraryVariants()
        }

        configureTest()
        configureDetekt()
    }
}
