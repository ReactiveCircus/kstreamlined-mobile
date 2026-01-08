package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.ui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.core.ui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.core.ui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.core.ui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.EmptyUi
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerRoute
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api.KotlinWeeklyIssueRoute
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api.KotlinWeeklyIssueSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.settings.api.SettingsRoute
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeRoute
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiState

@Composable
public fun SharedTransitionScope.SavedForLaterScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    backStack: NavBackStack<NavKey>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val presenter = metroViewModel<SavedForLaterViewModel>().presenter
    val uiState by presenter.states.collectAsStateWithLifecycle()
    val eventSink = presenter.eventSink
    SavedForLaterScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        listState = listState,
        onViewItem = { item ->
            backStack.add(
                when (item) {
                    is FeedItem.KotlinWeekly -> {
                        KotlinWeeklyIssueRoute(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                            issueNumber = item.issueNumber,
                        )
                    }
                    is FeedItem.TalkingKotlin -> {
                        TalkingKotlinEpisodeRoute(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                        )
                    }
                    else -> {
                        ContentViewerRoute(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                        )
                    }
                },
            )
        },
        onOpenSettings = {
            backStack.add(SettingsRoute(origin = SharedTransitionOrigin))
        },
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier,
    )
}

@Composable
internal fun SharedTransitionScope.SavedForLaterScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listState: LazyListState,
    onViewItem: (FeedItem) -> Unit,
    onOpenSettings: () -> Unit,
    uiState: SavedForLaterUiState,
    eventSink: (SavedForLaterUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = TopNavBarSharedTransitionKeys.bounds(SharedTransitionOrigin),
            titleElementKey = TopNavBarSharedTransitionKeys.titleElement(SharedTransitionOrigin),
            title = stringResource(id = R.string.title_saved_for_later),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    painter = KSIcons.Settings,
                    contentDescription = null,
                    onClick = onOpenSettings,
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
                        ContentUi(
                            animatedVisibilityScope = animatedVisibilityScope,
                            listState = listState,
                            items = uiState.feedItems,
                            onItemClick = onViewItem,
                            eventSink = eventSink,
                        )
                    } else {
                        EmptyUi(modifier = Modifier.padding(calculateListContentPadding()))
                    }
                }
            }
        }
    }
}

private const val SharedTransitionOrigin = "SavedForLater"

@Suppress("MaxLineLength")
@Composable
private fun SharedTransitionScope.ContentUi(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listState: LazyListState,
    items: List<DisplayableFeedItem<FeedItem>>,
    onItemClick: (FeedItem) -> Unit,
    eventSink: (SavedForLaterUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = calculateListContentPadding(),
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
                                sharedContentState = rememberSharedContentState(
                                    key = ContentViewerSharedTransitionKeys.bounds(
                                        origin = SharedTransitionOrigin,
                                        id = item.id,
                                    ),
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        saveButtonElementKey = ContentViewerSharedTransitionKeys.saveButtonElement(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                        ),
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
                                sharedContentState = rememberSharedContentState(
                                    key = KotlinWeeklyIssueSharedTransitionKeys.bounds(
                                        origin = SharedTransitionOrigin,
                                        id = item.id,
                                    ),
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
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
                                sharedContentState = rememberSharedContentState(
                                    key = ContentViewerSharedTransitionKeys.bounds(
                                        origin = SharedTransitionOrigin,
                                        id = item.id,
                                    ),
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        saveButtonElementKey = ContentViewerSharedTransitionKeys.saveButtonElement(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                        ),
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
                                sharedContentState = rememberSharedContentState(
                                    key = TalkingKotlinEpisodeSharedTransitionKeys.bounds(
                                        origin = SharedTransitionOrigin,
                                        id = item.id,
                                    ),
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        cardElementKey = TalkingKotlinEpisodeSharedTransitionKeys.playerElement(
                            origin = SharedTransitionOrigin,
                            id = item.id,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun calculateListContentPadding(): PaddingValues {
    return PaddingValues(
        top = 24.dp,
        start = 24.dp,
        end = 24.dp,
        bottom = 120.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
    )
}
