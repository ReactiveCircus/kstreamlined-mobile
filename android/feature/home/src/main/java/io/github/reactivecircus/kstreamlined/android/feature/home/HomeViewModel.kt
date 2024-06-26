package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    feedSyncEngine: FeedSyncEngine,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = HomePresenter(feedSyncEngine, feedDataSource, viewModelScope)
    val uiState: StateFlow<HomeUiState> = presenter.uiState
    val eventSink: (HomeUiEvent) -> Unit = presenter.eventSink
}
