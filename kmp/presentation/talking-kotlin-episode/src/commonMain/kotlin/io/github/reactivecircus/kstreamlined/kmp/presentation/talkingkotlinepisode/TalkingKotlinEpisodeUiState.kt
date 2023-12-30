package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

public sealed interface TalkingKotlinEpisodeUiState {
    public data object Initializing : TalkingKotlinEpisodeUiState
    public data class Content(
        val episode: FeedItem.TalkingKotlin
    ) : TalkingKotlinEpisodeUiState
}
