package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Apply and configure detekt.
 */
internal fun Project.configureDetekt() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", libs.plugin.detektFormatting)

    plugins.withType(DetektPlugin::class.java).configureEach {
        extensions.configure(DetektExtension::class.java) {
            it.source.from(files("src/"))
            it.config.from(files("${project.rootDir}/detekt.yml"))
            it.buildUponDefaultConfig = true
            it.allRules = true
            it.parallel = true
        }
        tasks.withType(Detekt::class.java).configureEach {
            it.jvmTarget = JvmTarget.JVM_17.target
            it.reports { report ->
                report.xml.required.set(false)
                report.txt.required.set(false)
                report.sarif.required.set(false)
                report.md.required.set(false)
            }
        }
    }
}
