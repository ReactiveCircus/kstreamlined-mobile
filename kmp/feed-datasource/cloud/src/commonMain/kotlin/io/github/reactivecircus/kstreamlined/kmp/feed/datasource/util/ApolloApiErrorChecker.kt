package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.util

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.NoDataException

public object ApolloApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean {
        return when (throwable) {
            // when using dataOrThrow() exceptions are wrapped in NoDataException
            is NoDataException -> throwable.cause.hasNetworkError()
            else -> throwable.hasNetworkError()
        }
    }

    private fun Throwable?.hasNetworkError(): Boolean {
        return when (this) {
            is ApolloNetworkException -> true
            else -> this?.suppressedExceptions?.any { it is ApolloNetworkException } == true
        }
    }
}