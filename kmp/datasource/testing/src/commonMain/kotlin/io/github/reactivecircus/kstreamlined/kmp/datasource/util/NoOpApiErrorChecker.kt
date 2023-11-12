package io.github.reactivecircus.kstreamlined.kmp.datasource.util

object NoOpApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean = false
}
