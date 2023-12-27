package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import io.github.reactivecircus.kstreamlined.android.feature.common.R as CommonR
import io.github.reactivecircus.kstreamlined.android.feature.common.openShareSheet
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState

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
            // TODO open in-app browser
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
                if (uiState is KotlinWeeklyIssueUiState.Content) {
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton(
                        if (uiState.savedForLater) {
                            KSIcons.BookmarkFill
                        } else {
                            KSIcons.BookmarkAdd
                        },
                        contentDescription = null,
                        onClick = onSaveButtonClick,
                    )
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
                            items = state.issueItems,
                            onItemClick = { onOpenLink(it.url) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentUi(
    items: List<KotlinWeeklyIssueItem>,
    onItemClick: (KotlinWeeklyIssueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(
            items,
            key = { item -> item.title + item.url + item.type },
        ) { item ->
            // TODO move
            Surface(
                onClick = { onItemClick(item) },
                shape = RoundedCornerShape(16.dp),
                color = KSTheme.colorScheme.container,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = item.title,
                        style = KSTheme.typography.titleMedium,
                        color = KSTheme.colorScheme.primary,
                    )
                    Text(
                        text = item.summary,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onBackgroundVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.source,
                        style = KSTheme.typography.labelLarge,
                        color = KSTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

// TODO update skeleton layout
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

private const val SkeletonItemCount = 10

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
