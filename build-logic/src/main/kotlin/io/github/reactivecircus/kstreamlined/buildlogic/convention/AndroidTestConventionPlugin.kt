package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.gradle.TestExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureAndroidTestExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlin
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
            configureKotlin(target, enableExplicitApi = false)
        }

        extensions.configure<TestExtension> {
            configureAndroidTestExtension()
        }

        configureDetekt()
    }
}
