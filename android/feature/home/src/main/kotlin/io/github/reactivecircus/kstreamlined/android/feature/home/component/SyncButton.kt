package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Chip
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.R

@Composable
internal fun SyncButton(
    showSkeleton: Boolean,
    syncing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        enabled = !showSkeleton && !syncing,
        contentColor = if (showSkeleton) {
            KSTheme.colorScheme.container
        } else {
            KSTheme.colorScheme.primary
        },
    ) {
        var currentRotation by remember { mutableFloatStateOf(0f) }
        val rotation = remember(syncing) { Animatable(currentRotation) }
        LaunchedEffect(syncing) {
            if (syncing) {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(AnimationDurationMillis, easing = LinearEasing),
                    ),
                ) {
                    currentRotation = value
                }
            } else {
                if (currentRotation > 0f) {
                    rotation.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(
                            durationMillis = AnimationDurationMillis,
                            easing = LinearOutSlowInEasing,
                        ),
                    ) {
                        currentRotation = 0f
                    }
                }
            }
        }
        Icon(
            KSIcons.Sync,
            contentDescription = null,
            modifier = Modifier.rotate(rotation.value),
        )
        Text(
            text = stringResource(id = R.string.sync).uppercase(),
            style = KSTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.1.sp,
            ),
        )
    }
}

private const val AnimationDurationMillis = 1000

@Composable
@PreviewLightDark
private fun PreviewSyncButton() {
    KSTheme {
        Surface {
            SyncButton(
                showSkeleton = false,
                syncing = true,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewSyncButton_skeleton() {
    KSTheme {
        Surface {
            SyncButton(
                showSkeleton = true,
                syncing = true,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
