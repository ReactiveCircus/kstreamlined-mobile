package io.github.reactivecircus.kstreamlined.android

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey {
    @Serializable
    data class KotlinWeeklyIssue(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val titleElementKey: String,
        val id: String,
        val issueNumber: Int,
    ) : NavRoute
}
