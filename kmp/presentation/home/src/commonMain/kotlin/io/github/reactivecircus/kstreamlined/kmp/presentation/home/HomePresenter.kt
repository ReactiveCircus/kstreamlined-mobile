package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.SyncState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public class HomePresenter(
    private val feedSyncEngine: FeedSyncEngine,
    feedDataSource: FeedDataSource,
    scope: CoroutineScope,
) {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    public val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        combine(
            feedSyncEngine.syncState,
            feedDataSource.streamFeedOrigins(),
            feedDataSource.streamFeedItemsForSelectedOrigins(),
        ) { syncState, feedOrigins, feedItems ->
            when (syncState) {
                is SyncState.Initializing, is SyncState.Syncing -> {
                    if (feedOrigins.isEmpty() || feedItems.isEmpty()) {
                        HomeUiState.Loading
                    } else {
                        HomeUiState.Content(
                            selectedFeedCount = feedOrigins.count { it.selected },
                            feedItems = feedItems.toHomeFeedItems(),
                            refreshing = true,
                            hasTransientError = false,
                        )
                    }
                }
                is SyncState.Idle -> {
                    HomeUiState.Content(
                        selectedFeedCount = feedOrigins.count { it.selected },
                        feedItems = feedItems.toHomeFeedItems(),
                        refreshing = false,
                        hasTransientError = false,
                    )
                }
                is SyncState.Error -> {
                    if (feedOrigins.isEmpty() || feedItems.isEmpty()) {
                        HomeUiState.Error
                    } else {
                        HomeUiState.Content(
                            selectedFeedCount = feedOrigins.count { it.selected },
                            feedItems = feedItems.toHomeFeedItems(),
                            refreshing = false,
                            hasTransientError = true,
                        )
                    }
                }
            }
        }.onEach {
            _uiState.value = it
        }.launchIn(scope)
    }

    public suspend fun refresh() {
        feedSyncEngine.sync(forceRefresh = true)
    }
}
