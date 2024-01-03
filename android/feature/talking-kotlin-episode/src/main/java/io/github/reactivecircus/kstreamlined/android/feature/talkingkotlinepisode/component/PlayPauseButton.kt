package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Chip
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Pause
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.R

@Composable
internal fun PlayPauseButton(
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = onPlayPauseButtonClick,
        modifier = modifier.animateContentSize(),
        contentColor = KSTheme.colorScheme.primary,
    ) {
        AnimatedContent(
            targetState = isPlaying,
            contentAlignment = Alignment.Center,
            label = "isPlaying",
        ) { playing ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (playing) {
                    true -> {
                        Icon(
                            KSIcons.Pause,
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(
                                id = R.string.pause
                            ),
                            style = KSTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )
                    }

                    false -> {
                        Icon(
                            KSIcons.PlayArrow,
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(
                                id = R.string.play
                            ),
                            style = KSTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewPlayPauseButton_playing() {
    KSTheme {
        Surface {
            PlayPauseButton(
                isPlaying = true,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewPlayPauseButton_paused() {
    KSTheme {
        Surface {
            PlayPauseButton(
                isPlaying = false,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
