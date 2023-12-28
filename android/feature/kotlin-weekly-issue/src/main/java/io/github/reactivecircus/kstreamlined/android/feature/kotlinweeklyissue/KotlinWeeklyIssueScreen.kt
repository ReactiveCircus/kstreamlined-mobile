package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Button
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.common.openCustomTab
import io.github.reactivecircus.kstreamlined.android.feature.common.openShareSheet
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component.IssueGroupUi
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component.IssueItemUi
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import io.github.reactivecircus.kstreamlined.android.feature.common.R as CommonR

@Composable
public fun KotlinWeeklyIssueScreen(
    id: String,
    issueNumber: Int,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel<KotlinWeeklyIssueViewModel>()
    LaunchedEffect(id) {
        viewModel.loadKotlinWeeklyIssue(id)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val title = stringResource(id = R.string.title_kotlin_weekly_issue, issueNumber)
    KotlinWeeklyIssueScreen(
        title = title,
        onNavigateUp = onNavigateUp,
        onShareButtonClick = {
            context.openShareSheet(title, id)
        },
        onSaveButtonClick = { /* TODO */ },
        onOpenLink = {
            context.openCustomTab(it)
        },
        onRetry = { viewModel.loadKotlinWeeklyIssue(id) },
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
internal fun KotlinWeeklyIssueScreen(
    title: String,
    onNavigateUp: () -> Unit,
    onShareButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onOpenLink: (url: String) -> Unit,
    onRetry: () -> Unit,
    uiState: KotlinWeeklyIssueUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            title = title,
            modifier = Modifier.zIndex(1f),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Share,
                    contentDescription = null,
                    onClick = onShareButtonClick,
                )
                AnimatedVisibility(visible = uiState is KotlinWeeklyIssueUiState.Content) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledIconButton(
                            if ((uiState as? KotlinWeeklyIssueUiState.Content)?.savedForLater == true) {
                                KSIcons.BookmarkFill
                            } else {
                                KSIcons.BookmarkAdd
                            },
                            contentDescription = null,
                            onClick = onSaveButtonClick,
                        )
                    }
                }
            },
        )

        Box {
            val transition = updateTransition(targetState = uiState, label = "uiState")
            transition.AnimatedContent(
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentAlignment = Alignment.Center,
                contentKey = { state -> state.contentKey }
            ) { state ->
                when (state) {
                    is KotlinWeeklyIssueUiState.InFlight -> {
                        LoadingSkeletonUi()
                    }

                    is KotlinWeeklyIssueUiState.Error -> {
                        ErrorUi(onRetry = onRetry)
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

@Composable
private fun ErrorUi(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            CommonR.drawable.ic_kodee_broken_hearted,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = CommonR.string.error_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = "Retry",
            onClick = onRetry,
        )
    }
}

private val KotlinWeeklyIssueUiState.contentKey: Int
    get() = when (this) {
        is KotlinWeeklyIssueUiState.InFlight -> 0
        is KotlinWeeklyIssueUiState.Error -> 1
        is KotlinWeeklyIssueUiState.Content -> 2
    }
