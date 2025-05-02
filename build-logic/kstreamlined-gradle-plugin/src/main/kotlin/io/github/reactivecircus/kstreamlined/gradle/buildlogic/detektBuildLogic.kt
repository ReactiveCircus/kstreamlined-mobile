package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Apply and configure detekt.
 */
internal fun Project.configureDetekt() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", the<LibrariesForLibs>().plugin.detektFormatting)

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            source.from(files("src/"))
            config.from(files("${project.rootDir}/detekt.yml"))
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
        }
        tasks.withType<Detekt>().configureEach {
            jvmTarget = JvmTarget.JVM_11.target
            reports {
                xml.required.set(false)
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
            }
        }
    }
}
