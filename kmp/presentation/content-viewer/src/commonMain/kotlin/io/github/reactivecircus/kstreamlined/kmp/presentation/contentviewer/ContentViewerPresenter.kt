package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@AssistedInject
public class ContentViewerPresenter(
    @Assisted private val id: String,
    private val feedDataSource: FeedDataSource,
    moleculeContext: MoleculeContext,
) : Presenter<ContentViewerUiEvent, ContentViewerUiState>(moleculeContext) {
    @Composable
    override fun present(): ContentViewerUiState {
        var uiState by remember { mutableStateOf<ContentViewerUiState>(ContentViewerUiState.Initializing) }
        LaunchedEffect(Unit) {
            feedDataSource.streamFeedItemById(id)
                .onStart { uiState = ContentViewerUiState.Initializing }
                .onEach { item ->
                    uiState = if (item != null) {
                        ContentViewerUiState.Content(item)
                    } else {
                        ContentViewerUiState.NotFound
                    }
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is ContentViewerUiEvent.ToggleSavedForLater -> {
                    val item = (uiState as? ContentViewerUiState.Content)?.item
                        ?: return@CollectEvent
                    if (!item.savedForLater) {
                        feedDataSource.addSavedFeedItem(item.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(item.id)
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
        public fun create(id: String): ContentViewerPresenter
    }
}
