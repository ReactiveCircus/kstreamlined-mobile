package io.github.reactivecircus.kstreamlined.android.feature.contentviewer

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = ContentViewerViewModel.Factory::class)
internal class ContentViewerViewModel @AssistedInject constructor(
    @Assisted id: String,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = ContentViewerPresenter(
        id = id,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
        recompositionMode = RecompositionMode.ContextClock,
    )
    val uiState: StateFlow<ContentViewerUiState> = presenter.states
    val eventSink: (ContentViewerUiEvent) -> Unit = presenter.eventSink

    @AssistedFactory
    interface Factory {
        fun create(id: String): ContentViewerViewModel
    }
}
