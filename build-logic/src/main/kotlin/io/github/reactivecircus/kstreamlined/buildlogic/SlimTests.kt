@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

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
    if (providers.gradleProperty(SLIM_TESTS_PROPERTY).isPresent) {
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
            beforeVariants(selector().withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.DEMO)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.MOCK)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
            beforeVariants(selector().withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.PROD)) {
                (it as HasUnitTestBuilder).enableUnitTest = false
            }
        }
    }
}

private const val SLIM_TESTS_PROPERTY = "slimTests"
