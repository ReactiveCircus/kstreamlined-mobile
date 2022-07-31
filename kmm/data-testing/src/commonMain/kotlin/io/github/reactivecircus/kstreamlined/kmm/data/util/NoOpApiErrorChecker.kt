package io.github.reactivecircus.kstreamlined.kmm.data.util

object NoOpApiErrorChecker : ApiErrorChecker {
    override fun isNetworkError(throwable: Throwable?): Boolean = false
}
