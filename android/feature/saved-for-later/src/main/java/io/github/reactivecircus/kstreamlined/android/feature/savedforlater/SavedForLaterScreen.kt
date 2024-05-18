@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiState
import io.github.reactivecircus.kstreamlined.android.feature.common.R as commonR

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
public fun SavedForLaterScreen(
    listState: LazyListState,
    onViewItem: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel<SavedForLaterViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink
    SavedForLaterScreen(
        listState = listState,
        onViewItem = onViewItem,
        uiState = uiState,
        eventSink,
        modifier = modifier,
    )
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
internal fun SavedForLaterScreen(
    listState: LazyListState,
    onViewItem: (FeedItem) -> Unit,
    uiState: SavedForLaterUiState,
    eventSink: (SavedForLaterUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = this@AnimatedVisibilityScope,
            boundsKey = "Bounds/Saved/TopBar",
            titleElementKey = "Element/Saved/TopBar/Title",
            title = stringResource(id = R.string.title_saved_for_later),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Settings,
                    contentDescription = null,
                    onClick = {},
                )
            },
        )
        if (uiState is SavedForLaterUiState.Content) {
            Box {
                AnimatedContent(
                    targetState = uiState.feedItems.isEmpty(),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.Center,
                    contentKey = { isEmpty -> if (isEmpty) 0 else 1 },
                    label = "isEmpty",
                ) { isEmpty ->
                    if (!isEmpty) {
                        with(this@AnimatedVisibilityScope) {
                            ContentUi(
                                listState = listState,
                                items = uiState.feedItems,
                                onItemClick = onViewItem,
                                eventSink = eventSink,
                            )
                        }
                    } else {
                        EmptyUi()
                    }
                }
            }
        }
    }
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@Suppress("MaxLineLength")
@Composable
private fun ContentUi(
    listState: LazyListState,
    items: List<DisplayableFeedItem<FeedItem>>,
    onItemClick: (FeedItem) -> Unit,
    eventSink: (SavedForLaterUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = ListContentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(
            items,
            key = { it.value.id },
            contentType = { it.value::class.simpleName },
        ) {
            val (item, displayablePublishTime) = it
            when (item) {
                is FeedItem.KotlinBlog -> {
                    KotlinBlogCard(
                        item = item.toDisplayable(displayablePublishTime),
                        onItemClick = onItemClick,
                        onSaveButtonClick = { eventSink(SavedForLaterUiEvent.RemoveSavedItem(item.id)) },
                        modifier = Modifier
                            .animateItem()
                            .sharedBounds(
                                rememberSharedContentState(key = "Bounds/Saved/${item.id}"),
                                animatedVisibilityScope = this@AnimatedVisibilityScope,
                            ),
                        animatedVisibilityScope = this@AnimatedVisibilityScope,
                        saveButtonElementKey = "Element/Saved/${item.id}/saveButton",
                    )
                }

                is FeedItem.KotlinWeekly -> {
                    KotlinWeeklyCard(
                        item = item.toDisplayable(displayablePublishTime),
                        onItemClick = onItemClick,
                        onSaveButtonClick = { eventSink(SavedForLaterUiEvent.RemoveSavedItem(item.id)) },
                        modifier = Modifier
                            .animateItem()
                            .sharedBounds(
                                rememberSharedContentState(key = "Bounds/Saved/${item.id}"),
                                animatedVisibilityScope = this@AnimatedVisibilityScope,
                            ),
                    )
                }

                is FeedItem.KotlinYouTube -> {
                    KotlinYouTubeCard(
                        item = item.toDisplayable(displayablePublishTime),
                        onItemClick = onItemClick,
                        onSaveButtonClick = { eventSink(SavedForLaterUiEvent.RemoveSavedItem(item.id)) },
                        modifier = Modifier
                            .animateItem()
                            .sharedBounds(
                                rememberSharedContentState(key = "Bounds/Saved/${item.id}"),
                                animatedVisibilityScope = this@AnimatedVisibilityScope,
                            ),
                        animatedVisibilityScope = this@AnimatedVisibilityScope,
                        saveButtonElementKey = "Element/Saved/${item.id}/saveButton",
                    )
                }

                is FeedItem.TalkingKotlin -> {
                    TalkingKotlinCard(
                        item = item.toDisplayable(displayablePublishTime),
                        onItemClick = onItemClick,
                        onSaveButtonClick = { eventSink(SavedForLaterUiEvent.RemoveSavedItem(item.id)) },
                        modifier = Modifier
                            .animateItem()
                            .sharedBounds(
                                rememberSharedContentState(key = "Bounds/Saved/${item.id}"),
                                animatedVisibilityScope = this@AnimatedVisibilityScope,
                            ),
                        animatedVisibilityScope = this@AnimatedVisibilityScope,
                        cardElementKey = "Element/Saved/${item.id}/player",
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyUi(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ListContentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            commonR.drawable.ic_kodee_lost,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = commonR.string.empty_state_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
    }
}

private val ListContentPadding = PaddingValues(
    top = 24.dp,
    start = 24.dp,
    end = 24.dp,
    bottom = 120.dp,
)
