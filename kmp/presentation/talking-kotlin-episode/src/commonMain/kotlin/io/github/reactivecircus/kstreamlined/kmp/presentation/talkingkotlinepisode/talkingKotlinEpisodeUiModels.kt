package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

public sealed interface TalkingKotlinEpisodeUiState {
    public data object Initializing : TalkingKotlinEpisodeUiState

    public data object NotFound : TalkingKotlinEpisodeUiState

    public data class Content(
        val episode: TalkingKotlinEpisode,
        val isPlaying: Boolean,
    ) : TalkingKotlinEpisodeUiState
}

public sealed interface TalkingKotlinEpisodeUiEvent {
    public data class LoadEpisode(val id: String) : TalkingKotlinEpisodeUiEvent

    public data object ToggleSavedForLater : TalkingKotlinEpisodeUiEvent

    public data class SaveStartPosition(val startPositionMillis: Long) : TalkingKotlinEpisodeUiEvent

    public data object TogglePlayPause : TalkingKotlinEpisodeUiEvent

    public data object Reset : TalkingKotlinEpisodeUiEvent
}
