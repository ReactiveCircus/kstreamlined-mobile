package io.github.reactivecircus.kstreamlined.kmp.test.utils

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.CoroutineContext

/**
 * Test util that executes [block] with [kotlinx.coroutines.test.runTest] with optional [before] and [after] blocks.
 * Adapted from https://github.com/apollographql/apollo-kotlin/blob/main/apollo-testing-support/src/commonMain/kotlin/com/apollographql/apollo3/testing/internal/runTest.kt.
 */
fun runTest(
    context: CoroutineContext = EmptyCoroutineContext,
    before: suspend CoroutineScope.() -> Unit = {},
    after: suspend CoroutineScope.() -> Unit = {},
    block: suspend CoroutineScope.() -> Unit = {},
) {
    kotlinx.coroutines.test.runTest(context) {
        before()
        try {
            block()
        } finally {
            after()
        }
    }
}
