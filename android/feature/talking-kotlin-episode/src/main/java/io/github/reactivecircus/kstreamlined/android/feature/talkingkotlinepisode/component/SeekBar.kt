package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import java.util.Locale
import kotlin.math.roundToInt

@Composable
internal fun SeekBar(
    progressMillis: Long,
    durationMillis: Long,
    onProgressChangeFinished: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var seeking by remember { mutableStateOf(false) }
    val trackHeight by animateDpAsState(
        targetValue = if (seeking) 12.dp else 4.dp,
        animationSpec = tween(
            durationMillis = AnimationDurationMillis,
            easing = LinearOutSlowInEasing,
        ),
        label = "height",
    )

    val timeLabelsOffset by animateDpAsState(
        targetValue = if (seeking) 20.dp else 12.dp,
        label = "offset",
        animationSpec = tween(
            durationMillis = AnimationDurationMillis,
            easing = LinearOutSlowInEasing,
        ),
    )
    val timeLabelsOffsetPx = with(LocalDensity.current) { timeLabelsOffset.toPx() }

    var currentProgressMillis by remember { mutableLongStateOf(progressMillis) }

    var fullTrackWidth by remember { mutableIntStateOf(0) }
    var activeTrackWidthPx by remember { mutableFloatStateOf(0f) }
    DisposableEffect(fullTrackWidth) {
        activeTrackWidthPx = (progressMillis.toFloat() / durationMillis.toFloat()) * fullTrackWidth
        onDispose { }
    }

    Box(
        modifier = modifier
            .height(24.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        seeking = true
                    },
                    onTap = {
                        seeking = false
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        seeking = false
                        onProgressChangeFinished(currentProgressMillis)
                    },
                    onDragCancel = {
                        seeking = false
                    },
                ) { _, dragAmount ->
                    activeTrackWidthPx =
                        (activeTrackWidthPx + dragAmount.x).coerceIn(0f, size.width.toFloat())
                    currentProgressMillis =
                        ((activeTrackWidthPx / size.width) * durationMillis).toLong()
                }
            },
    ) {
        Layout(
            {
                val inactiveColor = KSTheme.colorScheme.onBackgroundVariant
                val activeColor = KSTheme.colorScheme.onContainerInverse
                val activeSeekingColor = KSTheme.colorScheme.onTertiary
                val animatedActiveColor by animateColorAsState(
                    targetValue = if (seeking) activeSeekingColor else activeColor,
                    label = "color",
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                ) {
                    drawRect(
                        color = inactiveColor,
                        size = size,
                    )
                    drawRect(
                        color = animatedActiveColor,
                        size = size.copy(width = activeTrackWidthPx),
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
        ) { measurables, constraints ->
            val trackPlaceable = measurables.first().measure(constraints)
            fullTrackWidth = trackPlaceable.width
            layout(trackPlaceable.width, trackPlaceable.height) {
                trackPlaceable.placeRelative(0, 0)
            }
        }

        Row(
            modifier = Modifier.offset {
                IntOffset(0, timeLabelsOffsetPx.roundToInt())
            }
        ) {
            val (playedProgress, remainingProgress) = formatPlaybackTime(currentProgressMillis, durationMillis)
            Text(
                text = playedProgress,
                style = KSTheme.typography.labelSmall,
                color = KSTheme.colorScheme.onTertiaryVariant,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = remainingProgress,
                style = KSTheme.typography.labelSmall,
                color = KSTheme.colorScheme.onTertiaryVariant,
            )
        }
    }
}

@Suppress("MagicNumber")
private fun formatPlaybackTime(progressMillis: Long, durationMillis: Long): Pair<String, String> {
    val progressSeconds = progressMillis / 1000
    val progressString = String.format(
        Locale.ENGLISH,
        "%02d:%02d:%02d",
        progressSeconds / 3600,
        (progressSeconds % 3600) / 60,
        progressSeconds % 60,
    )

    val remainingSeconds = durationMillis / 1000 - progressMillis / 1000
    val remainingString = String.format(
        Locale.ENGLISH,
        "-%02d:%02d:%02d",
        remainingSeconds / 3600,
        (remainingSeconds % 3600) / 60,
        remainingSeconds % 60,
    )

    return progressString to remainingString
}

private const val AnimationDurationMillis = 400

@Composable
@PreviewLightDark
private fun PreviewSeekBar() {
    KSTheme {
        Surface(
            color = KSTheme.colorScheme.tertiary
        ) {
            @Suppress("MagicNumber")
            var progressMillis by remember { mutableLongStateOf(1200_000L) }
            SeekBar(
                modifier = Modifier.padding(8.dp),
                progressMillis = progressMillis,
                durationMillis = 3000_000L,
                onProgressChangeFinished = { progressMillis = it },
            )
        }
    }
}
