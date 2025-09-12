package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureCommonAndroidExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
        }

        extensions.configure(KotlinAndroidProjectExtension::class.java) {
            it.configureKotlin(target)
        }

        extensions.configure(LibraryExtension::class.java) {
            it.configureCommonAndroidExtension(target)
        }

        extensions.configure(LibraryAndroidComponentsExtension::class.java) {
            it.configureAndroidLibraryVariants()
        }

        configureTest()
        configureDetekt()
    }
}
