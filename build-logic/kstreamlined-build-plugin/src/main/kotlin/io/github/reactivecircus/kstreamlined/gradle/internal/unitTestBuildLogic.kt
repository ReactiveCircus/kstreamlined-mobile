package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent

/**
 * Configure JUnit test options if applicable.
 */
internal fun Project.configureTest() {
    tasks.withType(Test::class.java).configureEach { test ->
        test.testLogging {
            it.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}
