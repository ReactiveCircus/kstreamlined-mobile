package io.github.reactivecircus.kstreamlined.kmm.data.util

import com.apollographql.apollo3.exception.ApolloCompositeException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ApolloApiErrorCheckerTest {

    @Test
    fun `isNetworkError returns true when throwable is ApolloNetworkException`() {
        assertTrue(ApolloApiErrorChecker.isNetworkError(ApolloNetworkException()))
    }

    @Test
    fun `isNetworkError returns false when throwable is not ApolloNetworkException or ApolloCompositeException`() {
        assertFalse(ApolloApiErrorChecker.isNetworkError(ApolloParseException()))
        assertFalse(ApolloApiErrorChecker.isNetworkError(IllegalStateException()))
    }

    @Test
    fun `isNetworkError returns true when throwable is ApolloCompositeException and its first exception is ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                ApolloCompositeException(
                    first = ApolloNetworkException(),
                    second = CacheMissException("key", null),
                )
            )
        )
    }

    @Test
    fun `isNetworkError returns true when throwable is ApolloCompositeException and its second exception is ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                ApolloCompositeException(
                    first = CacheMissException("key", null),
                    second = ApolloNetworkException(),
                )
            )
        )
    }

    @Test
    fun `isNetworkError returns false when throwable is ApolloCompositeException and neither of its exceptions is ApolloNetworkException`() {
        assertFalse(
            ApolloApiErrorChecker.isNetworkError(
                ApolloCompositeException(
                    first = CacheMissException("key", null),
                    second = ApolloParseException(),
                )
            )
        )
    }

    @Test
    fun `isNetworkError returns false when throwable is null`() {
        assertFalse(ApolloApiErrorChecker.isNetworkError(null))
    }
}
