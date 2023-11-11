package io.github.reactivecircus.kstreamlined.kmp.data.util

interface ApiErrorChecker {

    /**
     * Returns whether the [throwable] is a network error
     * e.g. socket closed, DNS issue, TLS problem.
     */
    fun isNetworkError(throwable: Throwable?): Boolean
}
