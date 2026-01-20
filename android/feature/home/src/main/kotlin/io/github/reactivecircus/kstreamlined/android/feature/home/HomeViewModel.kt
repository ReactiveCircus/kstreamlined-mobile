package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomePresenter
import kotlinx.coroutines.CoroutineScope

@ViewModelKey(HomeViewModel::class)
@ContributesIntoMap(AppScope::class)
public class HomeViewModel(
    feedSyncEngine: FeedSyncEngine,
    feedDataSource: FeedDataSource,
) : ViewModel() {
    internal val presenter = HomePresenter(
        feedSyncEngine = feedSyncEngine,
        feedDataSource = feedDataSource,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
}
