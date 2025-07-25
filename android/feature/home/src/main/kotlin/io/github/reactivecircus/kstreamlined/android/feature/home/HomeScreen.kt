@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Horizontal
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.feature.home.component.FeedFilterChip
import io.github.reactivecircus.kstreamlined.android.feature.home.component.SyncButton
import io.github.reactivecircus.kstreamlined.android.feature.home.component.TransientErrorBanner
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Button
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeFeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiState
import kotlinx.coroutines.delay
import io.github.reactivecircus.kstreamlined.android.feature.common.R as commonR

@Composable
public fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listState: LazyListState,
    onViewItem: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink
    HomeScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        listState = listState,
        onViewItem = onViewItem,
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier,
    )
    ReportDrawnWhen { uiState !is HomeUiState.Loading }
}

@Composable
internal fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listState: LazyListState,
    onViewItem: (FeedItem) -> Unit,
    uiState: HomeUiState,
    eventSink: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = "Bounds/Home/TopBar",
            titleElementKey = "Element/Home/TopBar/Title",
            title = stringResource(id = R.string.title_home),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Settings,
                    contentDescription = null,
                    onClick = {},
                )
            },
            bottomRow = {
                FeedFilterChip(
                    showSkeleton = uiState !is HomeUiState.Content,
                    selectedFeedCount = if (uiState is HomeUiState.Content) uiState.selectedFeedCount else 0,
                    onClick = {},
                )
                Spacer(modifier = Modifier.width(8.dp))
                SyncButton(
                    showSkeleton = uiState !is HomeUiState.Content,
                    syncing = (uiState is HomeUiState.Content && uiState.refreshing),
                    onClick = { eventSink(HomeUiEvent.Refresh) },
                )
            }
        )

        Box {
            AnimatedContent(
                targetState = uiState,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentAlignment = Alignment.Center,
                contentKey = { state -> state.contentKey },
                label = "uiState",
            ) { state ->
                when (state) {
                    is HomeUiState.Loading -> {
                        LoadingSkeletonUi()
                    }

                    is HomeUiState.Error -> {
                        ErrorUi(eventSink = eventSink)
                    }

                    is HomeUiState.Content -> {
                        ContentUi(
                            animatedVisibilityScope = animatedVisibilityScope,
                            listState = listState,
                            items = state.feedItems,
                            showTransientError = state.hasTransientError,
                            onItemClick = onViewItem,
                            eventSink = eventSink,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.ContentUi(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listState: LazyListState,
    items: List<HomeFeedItem>,
    showTransientError: Boolean,
    onItemClick: (FeedItem) -> Unit,
    eventSink: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) = trace("FeedList") {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.testTag("home:feedList"),
            state = listState,
            contentPadding = calculateListContentPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(
                items,
                key = { item ->
                    when (item) {
                        is HomeFeedItem.SectionHeader -> item.title
                        is HomeFeedItem.Item -> item.displayableFeedItem.value.id
                    }
                },
                contentType = { item ->
                    when (item) {
                        is HomeFeedItem.SectionHeader -> HomeFeedItem.SectionHeader::class.simpleName
                        is HomeFeedItem.Item -> item.displayableFeedItem.value::class.simpleName
                    }
                },
            ) {
                when (it) {
                    is HomeFeedItem.SectionHeader -> {
                        trace("SectionHeader") {
                            Text(
                                text = it.title,
                                style = KSTheme.typography.titleMedium,
                                color = KSTheme.colorScheme.onBackgroundVariant,
                                modifier = Modifier.animateItem(),
                            )
                        }
                    }

                    is HomeFeedItem.Item -> {
                        val (item, displayablePublishTime) = it.displayableFeedItem
                        when (item) {
                            is FeedItem.KotlinBlog -> {
                                KotlinBlogCard(
                                    item = item.toDisplayable(displayablePublishTime),
                                    onItemClick = onItemClick,
                                    onSaveButtonClick = {
                                        eventSink(HomeUiEvent.ToggleSavedForLater(item))
                                    },
                                    modifier = Modifier
                                        .animateItem()
                                        .sharedBounds(
                                            rememberSharedContentState(key = "Bounds/Home/${item.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    saveButtonElementKey = "Element/Home/${item.id}/saveButton",
                                )
                            }

                            is FeedItem.KotlinWeekly -> {
                                KotlinWeeklyCard(
                                    item = item.toDisplayable(displayablePublishTime),
                                    onItemClick = onItemClick,
                                    onSaveButtonClick = {
                                        eventSink(HomeUiEvent.ToggleSavedForLater(item))
                                    },
                                    modifier = Modifier
                                        .animateItem()
                                        .sharedBounds(
                                            rememberSharedContentState(key = "Bounds/Home/${item.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        ),
                                )
                            }

                            is FeedItem.KotlinYouTube -> {
                                KotlinYouTubeCard(
                                    item = item.toDisplayable(displayablePublishTime),
                                    onItemClick = onItemClick,
                                    onSaveButtonClick = {
                                        eventSink(HomeUiEvent.ToggleSavedForLater(item))
                                    },
                                    modifier = Modifier
                                        .animateItem()
                                        .sharedBounds(
                                            rememberSharedContentState(key = "Bounds/Home/${item.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    saveButtonElementKey = "Element/Home/${item.id}/saveButton",
                                )
                            }

                            is FeedItem.TalkingKotlin -> {
                                TalkingKotlinCard(
                                    item = item.toDisplayable(displayablePublishTime),
                                    onItemClick = onItemClick,
                                    onSaveButtonClick = {
                                        eventSink(HomeUiEvent.ToggleSavedForLater(item))
                                    },
                                    modifier = Modifier
                                        .animateItem()
                                        .sharedBounds(
                                            rememberSharedContentState(key = "Bounds/Home/${item.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    cardElementKey = "Element/Home/${item.id}/player",
                                )
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showTransientError,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            TransientErrorBanner(onDismiss = { eventSink(HomeUiEvent.DismissTransientError) })
        }
        LaunchedEffect(showTransientError) {
            if (showTransientError) {
                delay(TransientErrorDurationMillis)
                eventSink(HomeUiEvent.DismissTransientError)
            }
        }
    }
}

private const val TransientErrorDurationMillis = 3000L

@Composable
private fun LoadingSkeletonUi(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        repeat(SkeletonItemCount) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                color = KSTheme.colorScheme.container,
            ) {}
        }
    }
}

private const val SkeletonItemCount = 10

@Composable
private fun ErrorUi(
    eventSink: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(calculateListContentPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            commonR.drawable.ic_kodee_broken_hearted,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = commonR.string.error_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = stringResource(id = commonR.string.retry),
            onClick = { eventSink(HomeUiEvent.Refresh) },
        )
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

private val HomeUiState.contentKey: Int
    get() = when (this) {
        is HomeUiState.Loading -> 0
        is HomeUiState.Error -> 1
        is HomeUiState.Content -> 2
    }
