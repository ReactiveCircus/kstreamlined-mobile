package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import androidx.compose.runtime.Immutable

@Immutable
public sealed interface TldrSummaryUiState {
    public data object Loading : TldrSummaryUiState

    public data object Error : TldrSummaryUiState

    public data class Content(val summary: TldrSummary) : TldrSummaryUiState
}

public sealed interface TldrSummaryUiEvent {
    public data object Retry : TldrSummaryUiEvent
}
