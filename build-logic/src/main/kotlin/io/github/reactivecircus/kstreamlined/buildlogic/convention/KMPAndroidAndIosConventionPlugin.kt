package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidLibraryVariants
import io.github.reactivecircus.kstreamlined.buildlogic.configureCommonAndroidOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKMPCommon
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlinCompileOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KMPAndroidAndIosConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("com.android.library")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            configureKMPCommon(target, enableJvmTarget = false, enableAndroidTarget = true)
        }

        extensions.configure<LibraryExtension> {
            configureCommonAndroidOptions(target)
        }

        extensions.configure<LibraryAndroidComponentsExtension> {
            configureAndroidLibraryVariants()
        }

        configureKotlinCompileOptions()
        configureTest()
        configureDetekt()
    }
}
