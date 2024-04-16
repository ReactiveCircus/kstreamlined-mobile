package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class SavedForLaterViewModel @Inject constructor(
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = SavedForLaterPresenter(feedDataSource, viewModelScope)
    val uiState: StateFlow<SavedForLaterUiState> = presenter.uiState
    val eventSink: (SavedForLaterUiEvent) -> Unit = presenter.eventSink
}
