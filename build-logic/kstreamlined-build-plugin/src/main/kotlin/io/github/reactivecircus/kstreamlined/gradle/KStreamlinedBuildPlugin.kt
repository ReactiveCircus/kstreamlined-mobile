package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
internal class KStreamlinedBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            KStreamlinedExtension::class.java,
            "kstreamlined",
            KStreamlinedExtensionImpl::class.java,
        ) as KStreamlinedExtensionImpl

        target.afterEvaluate {
            extension.validate()
        }
    }
}
