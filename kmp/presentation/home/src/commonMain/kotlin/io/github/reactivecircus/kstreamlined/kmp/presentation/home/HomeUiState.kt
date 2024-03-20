package io.github.reactivecircus.kstreamlined.kmp.presentation.home

public sealed interface HomeUiState {
    public data object Loading : HomeUiState
    public data class Content(
        val selectedFeedCount: Int,
        val feedItems: List<HomeFeedItem>,
        val refreshing: Boolean,
        val hasTransientError: Boolean,
    ) : HomeUiState

    public data object Error : HomeUiState
}
