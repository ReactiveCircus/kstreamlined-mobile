package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.feature.common.openCustomTab
import io.github.reactivecircus.kstreamlined.android.feature.common.openShareSheet
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component.PlayPauseButton
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component.PodcastPlayer
import io.github.reactivecircus.kstreamlined.android.foundation.composeutils.findUrl
import io.github.reactivecircus.kstreamlined.android.foundation.composeutils.linkify
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.HorizontalDivider
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
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
        onSaveButtonClick = { /* TODO */ },
        onShareButtonClick = { title, url ->
            context.openShareSheet(title, url)
        },
        onPlayPauseButtonClick = { viewModel.togglePlayPause() },
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
    onPlayPauseButtonClick: () -> Unit,
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
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
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
                        isPlaying = uiState.isPlaying,
                        onPlayPauseButtonClick = onPlayPauseButtonClick,
                        onOpenLink = onOpenLink,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentUi(
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    onOpenLink: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = episode.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(ImageSize)
                            .clip(RoundedCornerShape(8.dp)),
                        placeholder = ColorPainter(KSTheme.colorScheme.container),
                        error = ColorPainter(KSTheme.colorScheme.container),
                    )
                }
            }
            item {
                Text(
                    text = "${episode.displayablePublishTime} • ${episode.duration}",
                    style = KSTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = KSTheme.colorScheme.onBackgroundVariant,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = episode.title,
                    style = KSTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                )
                AnimatedContent(
                    targetState = isPlaying,
                    modifier = Modifier.padding(top = 8.dp),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.Center,
                    label = "isPlaying",
                ) { playing ->
                    PlayPauseButton(
                        isPlaying = playing,
                        onPlayPauseButtonClick = onPlayPauseButtonClick,
                    )
                }
            }
            item {
                val linkStyle = SpanStyle(
                    color = KSTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                val annotatedString = remember(episode.summary) {
                    episode.summary.linkify(linkStyle)
                }
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.findUrl(offset)?.let(onOpenLink)
                    },
                    style = KSTheme.typography.bodyMedium.copy(
                        color = KSTheme.colorScheme.onBackgroundVariant,
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Surface(
                    onClick = { onOpenLink(episode.contentUrl) },
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = KSTheme.colorScheme.primary,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 16.dp,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.episode_website),
                            style = KSTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(KSIcons.ArrowRight, contentDescription = null)
                    }
                }
            }
        }

        PodcastPlayer(
            episode = episode,
            isPlaying = isPlaying,
            onPlayPauseButtonClick = onPlayPauseButtonClick,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        )
    }
}

private val ImageSize = 240.dp

@Composable
private fun LoadingUi(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize())
}
