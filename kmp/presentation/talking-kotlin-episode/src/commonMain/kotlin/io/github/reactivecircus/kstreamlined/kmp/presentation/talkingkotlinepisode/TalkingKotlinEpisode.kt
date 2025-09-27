package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import androidx.compose.runtime.Immutable

@Immutable
public data class TalkingKotlinEpisode(
    val id: String,
    val title: String,
    val displayablePublishTime: String,
    val contentUrl: String,
    val savedForLater: Boolean,
    val audioUrl: String,
    val thumbnailUrl: String,
    val summary: String,
    val summaryIsHtml: Boolean,
    val duration: String,
    val startPositionMillis: Long,
)
