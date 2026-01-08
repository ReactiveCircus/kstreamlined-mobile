package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomePresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(HomeViewModel::class)
@ContributesIntoMap(AppScope::class)
public class HomeViewModel(
    feedSyncEngine: FeedSyncEngine,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    private val presenter = HomePresenter(
        feedSyncEngine = feedSyncEngine,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    internal val uiState: StateFlow<HomeUiState> = presenter.states
    internal val eventSink: (HomeUiEvent) -> Unit = presenter.eventSink
}
