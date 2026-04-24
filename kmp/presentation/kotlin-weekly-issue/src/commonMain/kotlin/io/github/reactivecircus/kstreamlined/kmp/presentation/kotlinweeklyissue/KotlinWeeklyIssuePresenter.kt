package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactoryKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@AssistedInject
public class KotlinWeeklyIssuePresenter(
    @Assisted private val id: String,
    private val feedDataSource: FeedDataSource,
    moleculeContext: MoleculeContext,
) : Presenter<KotlinWeeklyIssueUiEvent, KotlinWeeklyIssueUiState>(moleculeContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun present(): KotlinWeeklyIssueUiState {
        var uiState by remember { mutableStateOf<KotlinWeeklyIssueUiState>(KotlinWeeklyIssueUiState.Loading) }
        var loadCount by remember { mutableIntStateOf(0) }
        LaunchedEffect(loadCount) {
            feedDataSource.streamFeedItemById(id)
                .onStart { uiState = KotlinWeeklyIssueUiState.Loading }
                .mapLatest { item ->
                    item ?: error("Feed item not found")
                    item to feedDataSource.loadKotlinWeeklyIssue(item.contentUrl)
                }
                .onEach { (item = first, issue = second) ->
                    uiState = KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = issue.groupBy { it.group },
                        savedForLater = item.savedForLater,
                    )
                }
                .catch {
                    uiState = KotlinWeeklyIssueUiState.Error
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is KotlinWeeklyIssueUiEvent.Refresh -> {
                    loadCount++
                    uiState = KotlinWeeklyIssueUiState.Loading
                }

                is KotlinWeeklyIssueUiEvent.ToggleSavedForLater -> {
                    val state = uiState as? KotlinWeeklyIssueUiState.Content ?: return@CollectEvent
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

    @AssistedFactory
    @PresenterAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    public fun interface Factory : PresenterAssistedFactory {
        public fun create(id: String): KotlinWeeklyIssuePresenter
    }
}
