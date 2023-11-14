package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.util

interface ApiErrorChecker {

    /**
     * Returns whether the [throwable] is a network error
     * e.g. socket closed, DNS issue, TLS problem.
     */
    fun isNetworkError(throwable: Throwable?): Boolean
}
