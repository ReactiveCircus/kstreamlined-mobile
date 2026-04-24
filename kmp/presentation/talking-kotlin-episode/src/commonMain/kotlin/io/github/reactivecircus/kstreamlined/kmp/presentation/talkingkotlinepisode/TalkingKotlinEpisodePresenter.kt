package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

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
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@AssistedInject
public class TalkingKotlinEpisodePresenter(
    @Assisted private val id: String,
    private val feedDataSource: FeedDataSource,
    moleculeContext: MoleculeContext,
) : Presenter<TalkingKotlinEpisodeUiEvent, TalkingKotlinEpisodeUiState>(moleculeContext) {
    @Composable
    override fun present(): TalkingKotlinEpisodeUiState {
        var uiState by remember {
            mutableStateOf<TalkingKotlinEpisodeUiState>(TalkingKotlinEpisodeUiState.Initializing)
        }
        LaunchedEffect(Unit) {
            feedDataSource.streamFeedItemById(id)
                .onStart { uiState = TalkingKotlinEpisodeUiState.Initializing }
                .onEach { item ->
                    val talkingKotlinItem = item as? FeedItem.TalkingKotlin
                    uiState = if (talkingKotlinItem != null) {
                        TalkingKotlinEpisodeUiState.Content(
                            episode = talkingKotlinItem.asPresentationModel(),
                            isPlaying = false,
                        )
                    } else {
                        TalkingKotlinEpisodeUiState.NotFound
                    }
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is TalkingKotlinEpisodeUiEvent.ToggleSavedForLater -> {
                    val episode = (uiState as? TalkingKotlinEpisodeUiState.Content)?.episode
                        ?: return@CollectEvent
                    if (!episode.savedForLater) {
                        feedDataSource.addSavedFeedItem(episode.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(episode.id)
                    }
                }

                is TalkingKotlinEpisodeUiEvent.SaveStartPosition -> {
                    (uiState as? TalkingKotlinEpisodeUiState.Content)?.episode?.let { episode ->
                        feedDataSource.saveTalkingKotlinEpisodeStartPosition(
                            id = episode.id,
                            positionMillis = event.startPositionMillis,
                        )
                    }
                }

                is TalkingKotlinEpisodeUiEvent.TogglePlayPause -> {
                    val currentUiState = uiState
                    if (currentUiState is TalkingKotlinEpisodeUiState.Content) {
                        uiState = currentUiState.copy(isPlaying = !currentUiState.isPlaying)
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
        public fun create(id: String): TalkingKotlinEpisodePresenter
    }
}
