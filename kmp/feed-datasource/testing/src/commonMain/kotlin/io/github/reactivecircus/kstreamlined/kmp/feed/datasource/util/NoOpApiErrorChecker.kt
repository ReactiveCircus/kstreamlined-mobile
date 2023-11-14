package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.util

object NoOpApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean = false
}
