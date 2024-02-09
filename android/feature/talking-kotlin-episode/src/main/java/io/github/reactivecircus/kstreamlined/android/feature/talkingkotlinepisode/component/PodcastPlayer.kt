package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.foundation.composeutils.marqueeWithFadedEdges
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Pause
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode

@Composable
internal fun PodcastPlayer(
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        color = KSTheme.colorScheme.tertiary,
        contentColor = KSTheme.colorScheme.onTertiary,
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
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .marqueeWithFadedEdges(
                            edgeWidth = 12.dp,
                            iterations = if (isPlaying) Int.MAX_VALUE else 0,
                            delayMillis = 0,
                            velocity = 40.dp,
                        ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = episode.displayablePublishTime,
                    style = KSTheme.typography.bodySmall,
                )

                @Suppress("MagicNumber")
                var progressMillis by remember { mutableLongStateOf(1200_000L) }

                SeekBar(
                    progressMillis = progressMillis,
                    durationMillis = 3000_000L,
                    onProgressChangeFinished = {
                        progressMillis = it
                    },
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
@PreviewLightDark
private fun PreviewPodcastPlayer_paused() {
    KSTheme {
        Surface {
            PodcastPlayer(
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    duration = "35min.",
                ),
                isPlaying = false,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewPodcastPlayer_playing() {
    KSTheme {
        Surface {
            PodcastPlayer(
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    duration = "35min.",
                ),
                isPlaying = true,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
