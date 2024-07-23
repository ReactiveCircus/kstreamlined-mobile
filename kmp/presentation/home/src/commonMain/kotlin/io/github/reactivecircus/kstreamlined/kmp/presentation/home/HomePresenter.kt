package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.SyncState
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

public class HomePresenter(
    private val feedSyncEngine: FeedSyncEngine,
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<HomeUiEvent, HomeUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): HomeUiState {
        var uiState by remember { mutableStateOf<HomeUiState>(HomeUiState.Loading) }
        LaunchedEffect(Unit) {
            uiStateFlow(currentState = { uiState })
                .onEach { uiState = it }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is HomeUiEvent.ToggleSavedForLater -> {
                    if (!event.item.savedForLater) {
                        feedDataSource.addSavedFeedItem(event.item.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(event.item.id)
                    }
                }

                HomeUiEvent.Refresh -> {
                    feedSyncEngine.sync(forceRefresh = true)
                }

                HomeUiEvent.DismissTransientError -> {
                    val currentUiState = uiState
                    if (currentUiState is HomeUiState.Content) {
                        uiState = currentUiState.copy(hasTransientError = false)
                    }
                }
            }
        }
        return uiState
    }

    private fun uiStateFlow(
        currentState: () -> HomeUiState,
    ): Flow<HomeUiState> = combineWithMetadata(
        feedSyncEngine.syncState,
        feedDataSource.streamFeedOrigins(),
        feedDataSource.streamFeedItemsForSelectedOrigins(),
    ) { syncState, feedOrigins, feedItems, (isFirstTransform, lastEmittedFlowIndex) ->
        val hasContent = feedOrigins.isNotEmpty() && feedItems.isNotEmpty()
        when (syncState) {
            is SyncState.Syncing -> {
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

            is SyncState.OutOfSync -> {
                if (hasContent) {
                    /**
                     * Show transient error if:
                     * - this is the first transformation
                     * - or a new emission of [SyncState.OutOfSync] has triggered the current state transformation
                     * - or current state is [HomeUiState.Content] with `hasTransientError = true`
                     */
                    val hasTransientError = isFirstTransform ||
                        lastEmittedFlowIndex == 0 ||
                        (currentState() as? HomeUiState.Content)?.hasTransientError == true

                    HomeUiState.Content(
                        selectedFeedCount = feedOrigins.count { it.selected },
                        feedItems = feedItems.toHomeFeedItems(),
                        refreshing = false,
                        hasTransientError = hasTransientError,
                    )
                } else {
                    HomeUiState.Error
                }
            }
        }
    }
}
