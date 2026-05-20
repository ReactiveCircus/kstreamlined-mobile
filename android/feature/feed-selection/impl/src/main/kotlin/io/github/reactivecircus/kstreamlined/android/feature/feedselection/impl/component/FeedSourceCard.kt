package io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Switch
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin

@Composable
internal fun FeedSourceCard(
    origin: FeedOrigin,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectionProgress by animateFloatAsState(
        targetValue = if (origin.selected) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "selectionProgress"
    )

    val scale by animateFloatAsState(
        targetValue = if (origin.selected) 1f else 0.98f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val targetContentColor = if (origin.selected) {
        when (origin.key) {
            FeedOrigin.Key.KotlinBlog -> Color.White
            FeedOrigin.Key.KotlinYouTubeChannel -> KSTheme.colorScheme.onSurfaceYouTube
            FeedOrigin.Key.TalkingKotlinPodcast -> KSTheme.colorScheme.onSurfaceInverse
            FeedOrigin.Key.KotlinWeekly -> KSTheme.colorScheme.onAccent
        }
    } else {
        KSTheme.colorScheme.onBackground
    }

    val targetMutedContentColor = if (origin.selected) {
        when (origin.key) {
            FeedOrigin.Key.KotlinBlog -> Color.White.copy(alpha = 0.7f)
            FeedOrigin.Key.KotlinYouTubeChannel -> KSTheme.colorScheme.onSurfaceYouTubeMuted
            FeedOrigin.Key.TalkingKotlinPodcast -> KSTheme.colorScheme.onSurfaceInverseMuted
            FeedOrigin.Key.KotlinWeekly -> KSTheme.colorScheme.onAccentMuted
        }
    } else {
        KSTheme.colorScheme.onBackgroundMuted
    }

    val contentColor by animateColorAsState(targetValue = targetContentColor, label = "contentColor")
    val mutedContentColor by animateColorAsState(targetValue = targetMutedContentColor, label = "mutedContentColor")

    Surface(
        onClick = onToggle,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = 0.6f + 0.4f * selectionProgress
            },
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.surface,
    ) {
        Box {
            val brandModifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = selectionProgress }
                .clip(RoundedCornerShape(16.dp))

            when (origin.key) {
                FeedOrigin.Key.KotlinBlog -> {
                    Box(
                        modifier = brandModifier.background(
                            brush = Brush.horizontalGradient(KSTheme.colorScheme.brandGradient)
                        )
                    )
                }
                FeedOrigin.Key.KotlinYouTubeChannel -> {
                    Box(
                        modifier = brandModifier.background(
                            color = KSTheme.colorScheme.surfaceYouTube
                        )
                    )
                }
                FeedOrigin.Key.TalkingKotlinPodcast -> {
                    Box(
                        modifier = brandModifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    KSTheme.colorScheme.surfaceInverse,
                                    KSTheme.colorScheme.surfaceInverseMuted,
                                )
                            )
                        )
                    )
                }
                FeedOrigin.Key.KotlinWeekly -> {
                    Box(
                        modifier = brandModifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    KSTheme.colorScheme.accentStrong,
                                    KSTheme.colorScheme.accentSoft,
                                )
                            )
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = origin.title,
                        style = KSTheme.typography.titleLarge,
                        color = contentColor,
                    )
                    Text(
                        text = origin.description,
                        style = KSTheme.typography.bodyMedium,
                        color = mutedContentColor,
                    )
                }
                Switch(
                    selected = origin.selected,
                    onSelectedChange = { onToggle() },
                )
            }
        }
    }
}
