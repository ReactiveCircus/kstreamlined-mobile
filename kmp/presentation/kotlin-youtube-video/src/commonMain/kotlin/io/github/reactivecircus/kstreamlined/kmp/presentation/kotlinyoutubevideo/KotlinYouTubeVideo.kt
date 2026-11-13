package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

import androidx.compose.runtime.Immutable

@Immutable
public data class KotlinYouTubeVideo(
    val id: String,
    val title: String,
    val displayablePublishTime: String,
    val contentUrl: String,
    val savedForLater: Boolean,
    val thumbnailUrl: String,
    val description: String,
    val videoId: String,
)
