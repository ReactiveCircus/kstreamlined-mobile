package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import app.cash.paparazzi.gradle.PrepareResourcesTask
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.reactivecircus.chameleon.gradle.ChameleonExtension
import isIdeBuild
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import runningCheck
import runningPaparazzi

/**
 * Configure screenshot testing.
 */
internal fun Project.configureScreenshotTest() {
    pluginManager.apply("app.cash.paparazzi")
    pluginManager.withPlugin("app.cash.paparazzi") {
        tasks.withType(Test::class.java).configureEach { test ->
            if (!runningCheck) {
                // skip running regular unit tests when running one of the paparazzi tasks
                // skip running paparazzi tests when running regular unit tests
                test.filter {
                    if (runningPaparazzi) {
                        it.includePatterns += "*.snapshot*"
                    } else {
                        it.excludePatterns += "*.snapshot*"
                    }
                }
            }
        }
        tasks.withType(PrepareResourcesTask::class.java).configureEach {
            it.enabled = runningCheck || runningPaparazzi
        }
        tasks.named("check").configure {
            it.dependsOn("verifyPaparazzi")
        }
    }

    pluginManager.apply("app.cash.burst")

    pluginManager.apply("io.github.reactivecircus.chameleon")
    pluginManager.withPlugin("io.github.reactivecircus.chameleon") {
        @Suppress("MaxLineLength")
        extensions.configure(ChameleonExtension::class.java) {
            it.snapshotFunction.set(
                "io/github/reactivecircus/kstreamlined/android/core/screenshottesting/tester/SnapshotTester.snapshot",
            )
            it.themeVariantEnum.set(
                "io/github/reactivecircus/kstreamlined/android/core/screenshottesting/tester/ThemeVariant",
            )
        }
    }

    dependencies.add("testImplementation", project(":core:screenshot-testing:tester"))

    if (runningCheck || runningPaparazzi || isIdeBuild) {
        pluginManager.withPlugin("com.android.library") {
            extensions.configure(LibraryAndroidComponentsExtension::class.java) {
                it.beforeVariants { variantBuilder ->
                    (variantBuilder as HasUnitTestBuilder).enableUnitTest = true
                }
            }
            extensions.configure(LibraryExtension::class.java) {
                it.androidResources.enable = true
            }
        }
        pluginManager.withPlugin("com.android.application") {
            extensions.configure(ApplicationAndroidComponentsExtension::class.java) {
                it.beforeVariants { variantBuilder ->
                    (variantBuilder as HasUnitTestBuilder).enableUnitTest = true
                }
            }
        }
    }
}
