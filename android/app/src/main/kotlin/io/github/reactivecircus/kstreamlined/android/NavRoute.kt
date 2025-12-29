package io.github.reactivecircus.kstreamlined.android

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey {
    @Serializable
    data class ContentViewer(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val saveButtonElementKey: String,
        val id: String,
    ) : NavRoute

    @Serializable
    data class KotlinWeeklyIssue(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val titleElementKey: String,
        val id: String,
        val issueNumber: Int,
    ) : NavRoute

    @Serializable
    data class TalkingKotlinEpisode(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val playerElementKey: String,
        val id: String,
    ) : NavRoute

    @Serializable
    data class Settings(
        val topBarBoundsKey: String,
        val titleElementKey: String,
    ) : NavRoute
}
