package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Apply and configure detekt.
 */
internal fun Project.configureDetekt() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", libs.plugin.detektKtlintWrapper)

    plugins.withType(DetektPlugin::class.java).configureEach {
        extensions.configure(DetektExtension::class.java) {
            it.source.from(files("src/"))
            it.config.from(files("${project.rootDir}/detekt.yml"))
            it.buildUponDefaultConfig.set(true)
//            it.allRules.set(true)
            it.parallel.set(true)
        }
        tasks.withType(Detekt::class.java).configureEach {
            it.jvmTarget.set(JvmTarget.JVM_17.target)
            it.reports { report ->
                report.xml.required.set(false)
                report.sarif.required.set(false)
                report.md.required.set(false)
            }
        }
    }
}
