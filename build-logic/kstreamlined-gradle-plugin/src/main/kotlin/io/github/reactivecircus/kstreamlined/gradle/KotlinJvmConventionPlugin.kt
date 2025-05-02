package io.github.reactivecircus.kstreamlined.gradle

import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("com.android.lint")

        extensions.configure<KotlinJvmProjectExtension> {
            configureKotlin(target)
        }

        configureTest()
        configureDetekt()
    }
}
