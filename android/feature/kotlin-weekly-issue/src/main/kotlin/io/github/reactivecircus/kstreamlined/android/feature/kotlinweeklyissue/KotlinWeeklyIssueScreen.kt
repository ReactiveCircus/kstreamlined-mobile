@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.launcher.openCustomTab
import io.github.reactivecircus.kstreamlined.android.core.launcher.openShareSheet
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ErrorUi
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component.IssueGroupUi
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component.IssueItemUi
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState

@Composable
public fun SharedTransitionScope.KotlinWeeklyIssueScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsKey: String,
    topBarBoundsKey: String,
    titleElementKey: String,
    id: String,
    issueNumber: Int,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:KotlinWeeklyIssue") {
    val viewModel = hiltViewModel<KotlinWeeklyIssueViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    DisposableEffect(id) {
        eventSink(KotlinWeeklyIssueUiEvent.LoadIssue(id))
        onDispose { }
    }

    val context = LocalContext.current
    val title = stringResource(id = R.string.title_kotlin_weekly_issue, issueNumber)
    KotlinWeeklyIssueScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        topBarBoundsKey = topBarBoundsKey,
        titleElementKey = titleElementKey,
        id = id,
        title = title,
        onNavigateUp = onNavigateUp,
        onShareButtonClick = { url ->
            context.openShareSheet(title, url)
        },
        onOpenLink = {
            context.openCustomTab(it)
        },
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = boundsKey),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
    )
}

@Composable
internal fun SharedTransitionScope.KotlinWeeklyIssueScreen(
    topBarBoundsKey: String,
    titleElementKey: String,
    id: String,
    title: String,
    onNavigateUp: () -> Unit,
    onShareButtonClick: (String) -> Unit,
    onOpenLink: (url: String) -> Unit,
    uiState: KotlinWeeklyIssueUiState,
    eventSink: (KotlinWeeklyIssueUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = topBarBoundsKey,
            titleElementKey = titleElementKey,
            title = title,
            modifier = Modifier.zIndex(1f),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
            actions = {
                AnimatedVisibility(uiState is KotlinWeeklyIssueUiState.Content) {
                    Row {
                        val contentUrl = (uiState as? KotlinWeeklyIssueUiState.Content)?.contentUrl.orEmpty()
                        val saved = (uiState as? KotlinWeeklyIssueUiState.Content)?.savedForLater == true
                        FilledIconButton(
                            KSIcons.Share,
                            contentDescription = null,
                            onClick = { onShareButtonClick(contentUrl) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledIconButton(
                            if (saved) {
                                KSIcons.BookmarkFill
                            } else {
                                KSIcons.BookmarkAdd
                            },
                            contentDescription = null,
                            onClick = { eventSink(KotlinWeeklyIssueUiEvent.ToggleSavedForLater) },
                        )
                    }
                }
            },
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
                    is KotlinWeeklyIssueUiState.Loading -> {
                        LoadingSkeletonUi()
                    }

                    is KotlinWeeklyIssueUiState.Error -> {
                        ErrorUi(
                            onRetry = { eventSink(KotlinWeeklyIssueUiEvent.LoadIssue(id)) },
                            modifier = Modifier.padding(24.dp),
                        )
                    }

                    is KotlinWeeklyIssueUiState.Content -> {
                        ContentUi(
                            groupedItems = state.issueItems,
                            onItemClick = { onOpenLink(it.url) },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentUi(
    groupedItems: Map<KotlinWeeklyIssueItem.Group, List<KotlinWeeklyIssueItem>>,
    onItemClick: (KotlinWeeklyIssueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        groupedItems.forEach { (group, items) ->
            stickyHeader {
                IssueGroupUi(group = group)
            }

            items(
                items,
                key = { item -> item.title + item.url + item.group },
            ) { item ->
                IssueItemUi(
                    item = item,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

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
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                color = KSTheme.colorScheme.container,
            ) {}
        }
    }
}

private const val SkeletonItemCount = 5

private val KotlinWeeklyIssueUiState.contentKey: Int
    get() = when (this) {
        is KotlinWeeklyIssueUiState.Loading -> 0
        is KotlinWeeklyIssueUiState.Error -> 1
        is KotlinWeeklyIssueUiState.Content -> 2
    }
