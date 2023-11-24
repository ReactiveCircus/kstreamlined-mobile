package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Sync

@Composable
internal fun SyncButton(
    onClick: () -> Unit,
    syncing: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = !syncing,
        shape = CircleShape,
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.primaryOnContainer,
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            var currentRotation by remember { mutableStateOf(0f) }
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
                text = "Sync".uppercase(),
                style = KSTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                ),
            )
        }
    }
}

private const val AnimationDurationMillis = 1000
