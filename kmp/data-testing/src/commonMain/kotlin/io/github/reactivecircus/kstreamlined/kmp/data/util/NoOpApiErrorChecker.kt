package io.github.reactivecircus.kstreamlined.kmp.data.util

object NoOpApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean = false
}
