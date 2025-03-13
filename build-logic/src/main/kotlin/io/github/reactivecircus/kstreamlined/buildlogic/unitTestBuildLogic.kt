@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.withType

/**
 * Configure JUnit test options if applicable.
 */
internal fun Project.configureTest() {
    tasks.withType<Test>().configureEach {
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}
