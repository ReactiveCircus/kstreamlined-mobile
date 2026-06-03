package io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.contentPaddingHorizontal
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.ModalBottomSheet
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.rememberModalBottomSheetState
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.core.routemetadata.BottomSheetMetadataProvider
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.api.FeedSelectionRoute
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.component.FeedOriginCard
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.retainPresenter
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionUiState
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(FeedSelectionRoute::class, BottomSheetMetadataProvider::class)
@Composable
internal fun FeedSelectionBottomSheet(): Unit = trace("Screen:FeedSelection") {
    val presenter = retainPresenter<FeedSelectionPresenter>()
    val uiState by presenter.states.collectAsState()
    val eventSink = presenter.eventSink

    val state = uiState
    if (state is FeedSelectionUiState.Content) {
        FeedOriginList(
            feedOrigins = state.feedOrigins,
            onToggle = { key ->
                eventSink(FeedSelectionUiEvent.ToggleFeedOrigin(key))
            },
            modifier = Modifier.semantics { testTagsAsResourceId = true },
        )
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
internal fun FeedOriginList(
    feedOrigins: List<FeedOrigin>,
    onToggle: (FeedOrigin.Key) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .styleable {
                contentPaddingHorizontal(24.dp)
                contentPaddingBottom(24.dp)
            }
            .testTag("feedSelection:originList"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        feedOrigins.forEach { origin ->
            FeedOriginCard(
                origin = origin,
                onToggle = { onToggle(origin.key) },
            )
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewFeedSelectionBottomSheet_allSelected() {
    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(
                initiallyExpanded = true,
                skipPartiallyExpanded = true,
            ),
        ) {
            FeedOriginList(
                feedOrigins = listOf(
                    SampleFeedOrigin.KotlinBlog,
                    SampleFeedOrigin.KotlinYouTubeChannel,
                    SampleFeedOrigin.TalkingKotlinPodcast,
                    SampleFeedOrigin.KotlinWeekly,
                ),
                onToggle = {},
            )
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewFeedSelectionBottomSheet_someSelected() {
    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(
                initiallyExpanded = true,
                skipPartiallyExpanded = true,
            ),
        ) {
            FeedOriginList(
                feedOrigins = listOf(
                    SampleFeedOrigin.KotlinBlog.copy(selected = false),
                    SampleFeedOrigin.KotlinYouTubeChannel,
                    SampleFeedOrigin.TalkingKotlinPodcast.copy(selected = false),
                    SampleFeedOrigin.KotlinWeekly,
                ),
                onToggle = {},
            )
        }
    }
}
