package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Button
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.common.openCustomTab
import io.github.reactivecircus.kstreamlined.android.feature.common.openShareSheet
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState

@Composable
public fun TalkingKotlinEpisodeScreen(
    id: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel<TalkingKotlinEpisodeViewModel>()
    LaunchedEffect(id) {
        viewModel.loadTalkingKotlinEpisode(id)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    TalkingKotlinEpisodeScreen(
        title = "",
        onNavigateUp = onNavigateUp,
        onShareButtonClick = { title, url ->
            context.openShareSheet(title, url)
        },
        onSaveButtonClick = { /* TODO */ },
        onOpenLink = {
            context.openCustomTab(it)
        },
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
internal fun TalkingKotlinEpisodeScreen(
    title: String,
    onNavigateUp: () -> Unit,
    onShareButtonClick: (title: String, url: String) -> Unit,
    onSaveButtonClick: () -> Unit,
    onOpenLink: (url: String) -> Unit,
    uiState: TalkingKotlinEpisodeUiState,
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
                AnimatedVisibility(visible = uiState is TalkingKotlinEpisodeUiState.Content) {
                    Row {
                        val episode = (uiState as? TalkingKotlinEpisodeUiState.Content)?.episode
                        FilledIconButton(
                            KSIcons.Share,
                            contentDescription = null,
                            onClick = {
                                onShareButtonClick(
                                    episode?.title.orEmpty(),
                                    episode?.contentUrl.orEmpty(),
                                )
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledIconButton(
                            if (episode?.savedForLater == true) {
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
            when (uiState) {
                is TalkingKotlinEpisodeUiState.Initializing -> {
                    LoadingUi()
                }

                is TalkingKotlinEpisodeUiState.Content -> {
                    ContentUi(
                        episode = uiState.episode,
                        onOpenLink = onOpenLink,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentUi(
    episode: FeedItem.TalkingKotlin,
    onOpenLink: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(
                text = episode.title,
                style = KSTheme.typography.titleLarge,
            )
        }
        item {
            Text(
                text = episode.publishTime.toString(),
                style = KSTheme.typography.bodyMedium,
            )
        }
        item {
            Text(
                text = episode.duration,
                style = KSTheme.typography.labelLarge,
            )
        }
        item {
            Text(
                text = episode.summary,
                style = KSTheme.typography.bodyMedium,
            )
        }
        item {
            Button(
                text = "Episode website",
                onClick = { onOpenLink(episode.contentUrl) }
            )
        }
    }
}

@Composable
private fun LoadingUi(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize())
}
