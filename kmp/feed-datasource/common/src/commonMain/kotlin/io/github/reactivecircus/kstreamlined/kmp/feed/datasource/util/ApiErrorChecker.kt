package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.util

public interface ApiErrorChecker {

    /**
     * Returns whether the [throwable] is a network error
     * e.g. socket closed, DNS issue, TLS problem.
     */
    public fun isNetworkError(throwable: Throwable?): Boolean
}
