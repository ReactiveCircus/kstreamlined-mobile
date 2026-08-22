package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.core.ui.util.marqueeWithFadedEdges
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode

@OptIn(UnstableApi::class)
@Composable
internal fun PodcastPlayer(
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    onPlayerPositionChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val player = retainAudioPlayer(
        audioUrl = episode.audioUrl,
        startPositionMillis = episode.startPositionMillis,
        onPlaybackEnded = onPlayPauseButtonClick,
    )
    val progressState = rememberProgressStateWithTickInterval(player = player)
    val playerPositionMillis = progressState.currentPositionMs.toPlayerUiMillis()
    val playerDurationMillis = progressState.durationMs.toPlayerUiMillis()

    RetainedEffect(player) {
        onRetire {
            player?.release()
        }
    }

    SideEffect(player, isPlaying) {
        if (isPlaying) {
            player?.play()
        } else {
            player?.pause()
        }
    }

    val currentOnPlayerPositionChange = rememberUpdatedState(onPlayerPositionChange)
    LaunchedEffect(progressState) {
        snapshotFlow { progressState.currentPositionMs }
            .collect {
                currentOnPlayerPositionChange.value(it)
            }
    }

    PodcastPlayerUi(
        playerPositionMillis = playerPositionMillis,
        playerDurationMillis = playerDurationMillis,
        onPositionChange = { position ->
            player?.seekTo(position.toLong())
        },
        episode = episode,
        isPlaying = isPlaying,
        onPlayPauseButtonClick = onPlayPauseButtonClick,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

private fun Long.toPlayerUiMillis(): Int = coerceIn(0L, Int.MAX_VALUE.toLong()).toInt()

@Composable
internal fun PodcastPlayerUi(
    playerPositionMillis: Int,
    playerDurationMillis: Int,
    onPositionChange: (Int) -> Unit,
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        color = KSTheme.colorScheme.surfaceInverse,
        contentColor = KSTheme.colorScheme.onSurfaceInverse,
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = episode.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = episode.title,
                    style = KSTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    modifier = Modifier
                        .marqueeWithFadedEdges(
                            edgeWidth = 12.dp,
                            iterations = if (isPlaying) Int.MAX_VALUE else 0,
                            repeatDelayMillis = 0,
                            velocity = 40.dp,
                        ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = episode.displayablePublishTime,
                    style = KSTheme.typography.bodySmall,
                )

                SeekBar(
                    positionMillis = playerPositionMillis,
                    durationMillis = playerDurationMillis,
                    onPositionChangeFinished = onPositionChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            AnimatedContent(
                targetState = isPlaying,
                modifier = Modifier.padding(end = 8.dp),
                transitionSpec = { scaleIn() togetherWith scaleOut() },
                contentAlignment = Alignment.Center,
                label = "isPlaying",
            ) { playing ->
                LargeIconButton(
                    if (playing) KSIcons.Pause else KSIcons.PlayArrow,
                    contentDescription = null,
                    onClick = onPlayPauseButtonClick,
                )
            }
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewPodcastPlayerUi_paused() {
    PodcastPlayerUi(
        playerPositionMillis = 1200_000,
        playerDurationMillis = 3000_000,
        onPositionChange = {},
        episode = TalkingKotlinEpisode(
            id = "1",
            title = "Talking Kotlin Episode Title",
            displayablePublishTime = "03 Dec 2023",
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio-url",
            thumbnailUrl = "podcast-logo-url",
            summary = "summary",
            summaryIsHtml = false,
            duration = "35min.",
            startPositionMillis = 0,
        ),
        isPlaying = false,
        onPlayPauseButtonClick = {},
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewPodcastPlayer_playing() {
    PodcastPlayerUi(
        playerPositionMillis = 1200_000,
        playerDurationMillis = 3000_000,
        onPositionChange = {},
        episode = TalkingKotlinEpisode(
            id = "1",
            title = "Talking Kotlin Episode Title",
            displayablePublishTime = "03 Dec 2023",
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio-url",
            thumbnailUrl = "podcast-logo-url",
            summary = "summary",
            summaryIsHtml = false,
            duration = "35min.",
            startPositionMillis = 0,
        ),
        isPlaying = true,
        onPlayPauseButtonClick = {},
        modifier = Modifier.padding(8.dp),
    )
}
