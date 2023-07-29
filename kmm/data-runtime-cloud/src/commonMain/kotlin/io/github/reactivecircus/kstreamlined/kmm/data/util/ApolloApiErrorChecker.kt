package io.github.reactivecircus.kstreamlined.kmm.data.util

import com.apollographql.apollo3.exception.ApolloCompositeException
import com.apollographql.apollo3.exception.ApolloNetworkException

object ApolloApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean {
        return when (throwable) {
            is ApolloNetworkException -> true
            is ApolloCompositeException -> throwable.suppressedExceptions.any { it is ApolloNetworkException }
            else -> false
        }
    }
}
