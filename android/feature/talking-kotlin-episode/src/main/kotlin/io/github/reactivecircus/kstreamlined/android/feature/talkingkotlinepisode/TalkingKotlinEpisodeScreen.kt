@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.HorizontalDivider
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.launcher.openCustomTab
import io.github.reactivecircus.kstreamlined.android.core.launcher.openShareSheet
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ItemNotFoundUi
import io.github.reactivecircus.kstreamlined.android.core.ui.util.linkify
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component.PlayPauseButton
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component.PodcastPlayer
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState

@Composable
public fun SharedTransitionScope.TalkingKotlinEpisodeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    topBarBoundsKey: String,
    boundsKey: String,
    playerElementKey: String,
    id: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:TalkingKotlinEpisode") {
    val viewModel = hiltViewModel<TalkingKotlinEpisodeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    DisposableEffect(id) {
        eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode(id))
        onDispose { }
    }

    val context = LocalContext.current
    TalkingKotlinEpisodeScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        topBarBoundsKey = topBarBoundsKey,
        playerElementKey = playerElementKey,
        onNavigateUp = onNavigateUp,
        onShareButtonClick = { title, url ->
            context.openShareSheet(title, url)
        },
        onOpenLink = context::openCustomTab,
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = boundsKey),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
    )
}

@Composable
internal fun SharedTransitionScope.TalkingKotlinEpisodeScreen(
    topBarBoundsKey: String,
    playerElementKey: String,
    onNavigateUp: () -> Unit,
    onShareButtonClick: (title: String, url: String) -> Unit,
    onOpenLink: (url: String) -> Unit,
    uiState: TalkingKotlinEpisodeUiState,
    eventSink: (TalkingKotlinEpisodeUiEvent) -> Unit,
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
            title = "",
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
                    onClick = { eventSink(TalkingKotlinEpisodeUiEvent.ToggleSavedForLater) },
                )
            },
        )

        Box {
            when (uiState) {
                is TalkingKotlinEpisodeUiState.Initializing -> {
                    Box(modifier = modifier.fillMaxSize())
                }

                is TalkingKotlinEpisodeUiState.NotFound -> {
                    ItemNotFoundUi(modifier = Modifier.padding(24.dp))
                }

                is TalkingKotlinEpisodeUiState.Content -> {
                    ContentUi(
                        animatedVisibilityScope = animatedVisibilityScope,
                        playerElementKey = playerElementKey,
                        episode = uiState.episode,
                        eventSink = eventSink,
                        isPlaying = uiState.isPlaying,
                        onOpenLink = onOpenLink,
                    )
                }
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.ContentUi(
    animatedVisibilityScope: AnimatedVisibilityScope?,
    playerElementKey: String,
    episode: TalkingKotlinEpisode,
    eventSink: (TalkingKotlinEpisodeUiEvent) -> Unit,
    isPlaying: Boolean,
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
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
                    PlayPauseButton(
                        isPlaying = isPlaying,
                        onPlayPauseButtonClick = { eventSink(TalkingKotlinEpisodeUiEvent.TogglePlayPause) },
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
            item {
                val linkStyle = SpanStyle(
                    color = KSTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                val annotatedString = remember(episode.summary) {
                    if (episode.summaryIsHtml) {
                        AnnotatedString.fromHtml(episode.summary)
                    } else {
                        episode.summary.linkify(linkStyle)
                    }
                }
                Text(
                    text = annotatedString,
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
                                fontWeight = FontWeight.ExtraBold,
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
            onPlayPauseButtonClick = { eventSink(TalkingKotlinEpisodeUiEvent.TogglePlayPause) },
            onSaveStartPosition = { startPositionMillis ->
                eventSink(TalkingKotlinEpisodeUiEvent.SaveStartPosition(startPositionMillis))
            },
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            modifier = if (animatedVisibilityScope != null) {
                Modifier.sharedElement(
                    sharedContentState = rememberSharedContentState(key = playerElementKey),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            } else {
                Modifier
            },
        )
    }
}

private val ImageSize = 240.dp
