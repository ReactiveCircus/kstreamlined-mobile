package io.github.reactivecircus.kstreamlined.buildlogic.convention

import io.github.reactivecircus.kstreamlined.buildlogic.configureDetekt
import io.github.reactivecircus.kstreamlined.buildlogic.configureKMMCommon
import io.github.reactivecircus.kstreamlined.buildlogic.configureKotlinCommonCompileOptions
import io.github.reactivecircus.kstreamlined.buildlogic.configureTest
import io.github.reactivecircus.kstreamlined.buildlogic.markNonCompatibleConfigurationCacheTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KMMIosOnlyConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        extensions.configure<KotlinMultiplatformExtension> {
            configureKMMCommon(target, enableJvmTarget = false)
        }

        configureKotlinCommonCompileOptions()
        configureTest()
        configureDetekt()
        markNonCompatibleConfigurationCacheTasks()
    }
}
