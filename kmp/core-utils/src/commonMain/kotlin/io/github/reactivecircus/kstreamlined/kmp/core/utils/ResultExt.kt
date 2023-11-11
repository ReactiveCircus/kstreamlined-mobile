package io.github.reactivecircus.kstreamlined.kmp.core.utils

import kotlin.coroutines.cancellation.CancellationException

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception except [CancellationException] that was thrown from the
 * [block] function execution and encapsulating it as a failure.
 */
@Suppress("TooGenericExceptionCaught", "InstanceOfCheckForException")
inline fun <T, R> T.runCatchingNonCancellationException(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}
