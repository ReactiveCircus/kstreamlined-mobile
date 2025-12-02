package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Apply and configure detekt plugin.
 */
internal fun Project.configureDetekt() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", libs.plugin.detektKtlintWrapper)

    plugins.withType(DetektPlugin::class.java).configureEach {
        extensions.configure(DetektExtension::class.java) {
            it.source.setFrom(file("src/"))
            it.config.setFrom(file("${project.rootDir}/detekt.yml"))
            it.buildUponDefaultConfig.set(true)
            it.parallel.set(true)
        }
        tasks.withType(Detekt::class.java).configureEach {
            it.jvmTarget.set(JvmTarget.JVM_17.target)
            it.reports { report ->
                report.checkstyle.required.set(false)
                report.sarif.required.set(false)
                report.markdown.required.set(false)
            }
        }
    }
}
