package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

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
import kotlinx.datetime.TimeZone

@AssistedInject
public class KotlinYouTubeVideoPresenter(
    @Assisted private val id: String,
    private val feedDataSource: FeedDataSource,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    moleculeContext: MoleculeContext,
) : Presenter<KotlinYouTubeVideoUiEvent, KotlinYouTubeVideoUiState>(moleculeContext) {
    @Composable
    override fun present(): KotlinYouTubeVideoUiState {
        var uiState by remember {
            mutableStateOf<KotlinYouTubeVideoUiState>(KotlinYouTubeVideoUiState.Initializing)
        }
        LaunchedEffect(Unit) {
            feedDataSource.streamFeedItemById(id)
                .onStart { uiState = KotlinYouTubeVideoUiState.Initializing }
                .onEach { item ->
                    val youTubeItem = item as? FeedItem.KotlinYouTube
                    uiState = if (youTubeItem != null) {
                        KotlinYouTubeVideoUiState.Content(
                            video = context(timeZone) { youTubeItem.asPresentationModel() },
                        )
                    } else {
                        KotlinYouTubeVideoUiState.NotFound
                    }
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is KotlinYouTubeVideoUiEvent.ToggleSavedForLater -> {
                    val video = (uiState as? KotlinYouTubeVideoUiState.Content)?.video
                        ?: return@CollectEvent
                    if (!video.savedForLater) {
                        feedDataSource.addSavedFeedItem(video.id)
                    } else {
                        feedDataSource.removeSavedFeedItem(video.id)
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
        public fun create(id: String): KotlinYouTubeVideoPresenter
    }
}
