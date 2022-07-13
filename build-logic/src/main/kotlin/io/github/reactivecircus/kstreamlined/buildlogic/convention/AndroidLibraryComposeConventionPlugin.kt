package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.gradle.LibraryExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<LibraryExtension> {
            configureCompose(this)
        }
    }
}
