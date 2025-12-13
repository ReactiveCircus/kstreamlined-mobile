package io.github.reactivecircus.licentia.gradle

import app.cash.licensee.LicenseeTask
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized

public class LicentiaGradlePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        var androidAppPluginApplied = false
        var licenseePluginApplied = false
        pluginManager.withPlugin("com.android.application") {
            androidAppPluginApplied = true
            pluginManager.withPlugin("app.cash.licensee") {
                licenseePluginApplied = true
                extensions.configure(ApplicationAndroidComponentsExtension::class.java) { extension ->
                    extension.onVariants { variant ->
                        val generateLicensesInfoSource = tasks.register(
                            "generateLicensesInfoSource${variant.name.capitalized()}",
                            GenerateLicensesInfoSource::class.java,
                        ) { task ->
                            task.packageName.set(variant.namespace.map { "$it.licentia" })
                            task.artifactsJsonFile.set(
                                tasks.withType(LicenseeTask::class.java)
                                    .named { it.endsWith(variant.name, ignoreCase = true) }.single()
                                    .jsonOutput,
                            )
                            task.outputDir.set(layout.buildDirectory.dir("generated/source/licentia/${variant.name}"))
                        }
                        variant.sources.kotlin!!.addGeneratedSourceDirectory(
                            generateLicensesInfoSource,
                            GenerateLicensesInfoSource::outputDir,
                        )
                    }
                }
            }
            dependencies.add("implementation", "io.github.reactivecircus.licentia:licentia-runtime")
        }
        afterEvaluate {
            check(androidAppPluginApplied) {
                "Licentia requires the `com.android.application` plugin to be applied to the same project," +
                    " it's missing in $displayName."
            }
            check(licenseePluginApplied) {
                "Licentia requires the `app.cash.licensee` plugin to be applied to the same project," +
                    " it's missing in $displayName."
            }
        }
    }
}
