package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.kstreamlined.buildlogic.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<BaseAppModuleExtension> {
            configureCompose(this)
        }
    }
}
