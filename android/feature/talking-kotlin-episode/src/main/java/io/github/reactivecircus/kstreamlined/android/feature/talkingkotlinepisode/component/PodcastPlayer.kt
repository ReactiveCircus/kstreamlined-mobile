package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Pause
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode

@Composable
internal fun PodcastPlayer(
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val brush = Brush.horizontalGradient(
        colors = listOf(
            KSTheme.colorScheme.tertiary,
            KSTheme.colorScheme.tertiaryVariant,
        ),
    )
    Surface(
        modifier = modifier
            .drawBehind {
                val cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(0f, 0f),
                                size = size,
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                }
                drawPath(
                    path = path,
                    brush = brush,
                )
            }
            .fillMaxWidth()
            .padding(contentPadding),
        color = Color.Transparent,
        contentColor = KSTheme.colorScheme.onTertiary,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = episode.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
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
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = episode.displayablePublishTime,
                    style = KSTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedContent(
                targetState = isPlaying,
                modifier = Modifier.padding(end = 8.dp),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentAlignment = Alignment.Center,
                label = "isPlaying",
            ) { playing ->
                when (playing) {
                    true -> {
                        LargeIconButton(
                            KSIcons.Pause,
                            contentDescription = null,
                            onClick = onPlayPauseButtonClick,
                        )
                    }

                    false -> {
                        LargeIconButton(
                            KSIcons.PlayArrow,
                            contentDescription = null,
                            onClick = onPlayPauseButtonClick,
                        )
                    }
                }
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
