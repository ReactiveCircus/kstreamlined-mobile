package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.TestExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureAndroidTestExtension
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureBuiltInKotlin
import io.github.reactivecircus.kstreamlined.gradle.buildlogic.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

internal class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.test")
        }

        extensions.configure(KotlinBaseExtension::class.java) {
            it.configureBuiltInKotlin(target, enableExplicitApi = false)
        }

        extensions.configure(TestExtension::class.java) {
            it.configureAndroidTestExtension()
        }

        configureDetekt()
    }
}
