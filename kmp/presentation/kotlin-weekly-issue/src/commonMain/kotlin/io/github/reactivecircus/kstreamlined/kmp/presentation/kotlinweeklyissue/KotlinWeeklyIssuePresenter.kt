package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class KotlinWeeklyIssuePresenter(
    private val feedDataSource: FeedDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<KotlinWeeklyIssueUiEvent, KotlinWeeklyIssueUiState>(scope, recompositionMode) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun present(): KotlinWeeklyIssueUiState {
        var uiState by remember { mutableStateOf<KotlinWeeklyIssueUiState>(KotlinWeeklyIssueUiState.Loading) }
        var itemId by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(itemId) {
            itemId?.let { id ->
                feedDataSource.streamFeedItemById(id)
                    .onStart { uiState = KotlinWeeklyIssueUiState.Loading }
                    .mapLatest { item ->
                        item ?: error("Feed item not found")
                        item to feedDataSource.loadKotlinWeeklyIssue(item.contentUrl)
                    }
                    .onEach { (item, issue) ->
                        uiState = KotlinWeeklyIssueUiState.Content(
                            id = item.id,
                            contentUrl = item.contentUrl,
                            issueItems = issue.groupBy { it.group },
                            savedForLater = item.savedForLater
                        )
                    }
                    .catch {
                        uiState = KotlinWeeklyIssueUiState.Error
                    }.collect()
            }
        }
        CollectEvent { event ->
            when (event) {
                is KotlinWeeklyIssueUiEvent.LoadIssue -> {
                    itemId = event.id
                }
                is KotlinWeeklyIssueUiEvent.ToggleSavedForLater -> {
                    val state = (uiState as? KotlinWeeklyIssueUiState.Content) ?: return@CollectEvent
                    if (!state.savedForLater) {
                        feedDataSource.addSavedFeedItem(state.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(state.id)
                    }
                }
            }
        }
        return uiState
    }
}
