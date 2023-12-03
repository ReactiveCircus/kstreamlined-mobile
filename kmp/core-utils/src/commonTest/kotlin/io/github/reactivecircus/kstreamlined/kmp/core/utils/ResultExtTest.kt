package io.github.reactivecircus.kstreamlined.kmp.core.utils

import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ResultExtTest {

    @Test
    fun `runCatchingNonCancellationException catches non-CancellationException`() {
        val result = runCatchingNonCancellationException {
            throw IllegalArgumentException()
        }

        assertTrue(result.isFailure)
    }

    @Test
    fun `runCatchingNonCancellationException does not catch CancellationException`() {
        assertFailsWith<CancellationException> {
            runCatchingNonCancellationException {
                throw CancellationException()
            }
        }
    }
}
