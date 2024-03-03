package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

public sealed interface TalkingKotlinEpisodeUiState {
    public data object Initializing : TalkingKotlinEpisodeUiState
    public data object NotFound : TalkingKotlinEpisodeUiState
    public data class Content(
        val episode: TalkingKotlinEpisode,
        val isPlaying: Boolean,
    ) : TalkingKotlinEpisodeUiState
}
