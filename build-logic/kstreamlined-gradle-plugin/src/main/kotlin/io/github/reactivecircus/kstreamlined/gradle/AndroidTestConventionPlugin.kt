package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.TestExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidTestExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.test")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure(KotlinAndroidProjectExtension::class.java) {
            it.configureKotlin(target, enableExplicitApi = false)
        }

        extensions.configure(TestExtension::class.java) {
            it.configureAndroidTestExtension()
        }

        configureDetekt()
    }
}
