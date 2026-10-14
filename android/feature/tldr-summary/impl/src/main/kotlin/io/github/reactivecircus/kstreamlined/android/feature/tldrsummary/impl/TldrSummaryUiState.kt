package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
internal sealed interface TldrSummaryUiState {
    data object Loading : TldrSummaryUiState

    data object Error : TldrSummaryUiState

    data class Content(
        val summary: AnnotatedString,
        val model: String,
        val displayableGeneratedTime: String,
    ) : TldrSummaryUiState
}
