package io.github.reactivecircus.kstreamlined.kmp.datasource.util

import com.apollographql.apollo3.exception.ApolloGraphQLException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.NoDataException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ApolloApiErrorCheckerTest {

    @Test
    fun `isNetworkError returns true when throwable is ApolloNetworkException`() {
        assertTrue(ApolloApiErrorChecker.isNetworkError(ApolloNetworkException()))
    }

    @Test
    fun `isNetworkError returns true when throwable is NoDataException and its cause is ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                NoDataException(cause = ApolloNetworkException())
            )
        )
    }

    @Test
    fun `isNetworkError returns true when throwable is not ApolloNetworkException or NoDataException but it has suppressed ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                ApolloParseException().apply {
                    addSuppressed(CacheMissException("key", null))
                    addSuppressed(ApolloNetworkException())
                }
            )
        )
    }

    @Test
    fun `isNetworkError returns true when throwable is NoDataException and its cause is not ApolloNetworkException but has suppressed ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                NoDataException(
                    cause = ApolloParseException().apply {
                        addSuppressed(CacheMissException("key", null))
                        addSuppressed(ApolloNetworkException())
                    }
                )
            )
        )
    }

    @Test
    fun `isNetworkError returns false when neither throwable or its cause is ApolloNetworkException and there are no suppressed exceptions`() {
        assertFalse(ApolloApiErrorChecker.isNetworkError(ApolloParseException()))
        assertFalse(ApolloApiErrorChecker.isNetworkError(IllegalStateException()))
        assertFalse(ApolloApiErrorChecker.isNetworkError(NoDataException(ApolloParseException())))
    }

    @Test
    fun `isNetworkError returns true when throwable is neither NoDataException or ApolloNetworkException but it has suppressed ApolloNetworkException`() {
        assertTrue(
            ApolloApiErrorChecker.isNetworkError(
                ApolloParseException().apply {
                    addSuppressed(CacheMissException("key", null))
                    addSuppressed(ApolloNetworkException())
                }
            )
        )
    }

    @Test
    fun `isNetworkError returns false when throwable is neither NoDataException or ApolloNetworkException and no suppressed exceptions is ApolloNetworkException`() {
        assertFalse(
            ApolloApiErrorChecker.isNetworkError(
                ApolloParseException().apply {
                    addSuppressed(CacheMissException("key", null))
                    addSuppressed(ApolloGraphQLException(emptyList()))
                }
            )
        )
    }

    @Test
    fun `isNetworkError returns false when throwable is NoDataException and no suppressed exceptions of the cause is ApolloNetworkException`() {
        assertFalse(
            ApolloApiErrorChecker.isNetworkError(
                NoDataException(
                    cause = ApolloParseException().apply {
                        addSuppressed(CacheMissException("key", null))
                        addSuppressed(ApolloGraphQLException(emptyList()))
                    }
                )
            )
        )
    }

    @Test
    fun `isNetworkError returns false when throwable is null`() {
        assertFalse(ApolloApiErrorChecker.isNetworkError(null))
    }
}
