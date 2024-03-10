package io.github.reactivecircus.kstreamlined.android.feature.contentviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ContentViewerViewModel @Inject constructor(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = ContentViewerPresenter(feedDataSource)
    val uiState: StateFlow<ContentViewerUiState> = presenter.uiState

    fun loadContent(id: String) = viewModelScope.launch {
        presenter.loadContent(id)
    }

    fun toggleSavedForLater() = viewModelScope.launch {
        presenter.toggleSavedForLater()
    }
}
