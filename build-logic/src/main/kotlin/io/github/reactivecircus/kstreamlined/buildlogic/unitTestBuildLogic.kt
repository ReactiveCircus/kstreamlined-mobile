@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType

/**
 * Configure JUnit test options if applicable.
 */
internal fun Project.configureTest() {
    tasks.withType<Test>().configureEach {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

/**
 * When the "slimTests" project property is provided, disable the unit test tasks
 * on `release`, 'benchmark' build types and `demo`, `mock`, and `prod` product flavors to avoid running the same tests repeatedly
 * in different build variants.
 *
 * Examples:
 * `./gradlew test -PslimTests` will run unit tests for `devDebug` and `debug` build variants
 * in Android App and Library projects, and all tests in JVM projects and Kotlin Multiplatform projects.
 */
internal fun Project.configureSlimTests() {
    if (providers.gradleProperty(SlimTestsProperty).isPresent) {
        // disable unit test tasks on the release, benchmark build types for Android Library projects
        extensions.findByType<LibraryAndroidComponentsExtension>()?.run {
            beforeVariants(selector().withBuildType("release")) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withBuildType("benchmark")) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
        }

        // disable unit test tasks on the release, benchmark build types and all non-dev flavors for Android Application projects.
        extensions.findByType<ApplicationAndroidComponentsExtension>()?.run {
            beforeVariants(selector().withBuildType("release")) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withBuildType("benchmark")) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withFlavor(FlavorDimensions.Environment to ProductFlavors.Demo)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withFlavor(FlavorDimensions.Environment to ProductFlavors.Mock)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withFlavor(FlavorDimensions.Environment to ProductFlavors.Prod)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
        }
    }
}

private const val SlimTestsProperty = "slimTests"
