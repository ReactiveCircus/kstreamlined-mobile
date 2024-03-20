package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.SyncState
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public class HomePresenter(
    private val feedSyncEngine: FeedSyncEngine,
    private val feedDataSource: FeedDataSource,
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
            val hasContent = feedOrigins.isNotEmpty() && feedItems.isNotEmpty()
            when (syncState) {
                is SyncState.Initializing, is SyncState.Syncing -> {
                    if (hasContent) {
                        HomeUiState.Content(
                            selectedFeedCount = feedOrigins.count { it.selected },
                            feedItems = feedItems.toHomeFeedItems(),
                            refreshing = true,
                            hasTransientError = false,
                        )
                    } else {
                        HomeUiState.Loading
                    }
                }
                is SyncState.Idle -> {
                    if (hasContent) {
                        HomeUiState.Content(
                            selectedFeedCount = feedOrigins.count { it.selected },
                            feedItems = feedItems.toHomeFeedItems(),
                            refreshing = false,
                            hasTransientError = false,
                        )
                    } else {
                        HomeUiState.Loading
                    }
                }
                is SyncState.Error -> {
                    if (hasContent) {
                        HomeUiState.Content(
                            selectedFeedCount = feedOrigins.count { it.selected },
                            feedItems = feedItems.toHomeFeedItems(),
                            refreshing = false,
                            hasTransientError = true,
                        )
                    } else {
                        HomeUiState.Error
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

    public suspend fun toggleSavedForLater(feedItem: FeedItem) {
        if (!feedItem.savedForLater) {
            feedDataSource.addSavedFeedItem(feedItem.id)
        } else {
            feedDataSource.removeSavedFeedItem(feedItem.id)
        }
    }
}
