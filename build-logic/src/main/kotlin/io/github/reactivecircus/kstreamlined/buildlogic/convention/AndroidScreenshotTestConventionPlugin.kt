package io.github.reactivecircus.kstreamlined.buildlogic.convention

import app.cash.paparazzi.gradle.PrepareResourcesTask
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import isIdeBuild
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import runningCheck
import runningPaparazzi

internal class AndroidScreenshotTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.cash.paparazzi")
        pluginManager.withPlugin("app.cash.paparazzi") {
            tasks.withType<Test>().configureEach {
                if (!runningCheck) {
                    // skip running regular unit tests when running one of the paparazzi tasks
                    // skip running paparazzi tests when running regular unit tests
                    filter {
                        if (runningPaparazzi) {
                            includePatterns += "*.snapshot*"
                        } else {
                            excludePatterns += "*.snapshot*"
                        }
                    }
                }
            }
            tasks.withType<PrepareResourcesTask>().configureEach {
                enabled = runningCheck || runningPaparazzi
            }
            tasks.named("check").configure {
                dependsOn("verifyPaparazzi")
            }
        }

        dependencies.add("testImplementation", project(":foundation:screenshot-testing:tester"))

        if (runningCheck || runningPaparazzi || isIdeBuild) {
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryAndroidComponentsExtension> {
                    beforeVariants {
                        (it as HasUnitTestBuilder).enableUnitTest = true
                    }
                }
            }
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationAndroidComponentsExtension> {
                    beforeVariants {
                        (it as HasUnitTestBuilder).enableUnitTest = true
                    }
                }
            }
        }
    }
}
