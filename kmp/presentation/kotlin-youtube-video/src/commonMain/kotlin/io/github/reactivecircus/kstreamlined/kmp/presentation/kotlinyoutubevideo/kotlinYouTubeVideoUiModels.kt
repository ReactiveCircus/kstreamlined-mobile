package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

import androidx.compose.runtime.Immutable

@Immutable
public sealed interface KotlinYouTubeVideoUiState {
    public data object Initializing : KotlinYouTubeVideoUiState

    public data object NotFound : KotlinYouTubeVideoUiState

    public data class Content(val video: KotlinYouTubeVideo) : KotlinYouTubeVideoUiState
}

public sealed interface KotlinYouTubeVideoUiEvent {
    public data object ToggleSavedForLater : KotlinYouTubeVideoUiEvent
}
