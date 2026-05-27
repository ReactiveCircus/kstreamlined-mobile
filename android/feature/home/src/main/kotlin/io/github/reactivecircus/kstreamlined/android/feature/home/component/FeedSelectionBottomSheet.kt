package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.MeshGradientPainter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.ModalBottomSheet
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.SheetState
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Switch
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.rememberModalBottomSheetState
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin

@Composable
internal fun FeedSelectionBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val mockFeedOrigins = remember {
        mutableStateListOf(
            FeedOrigin(
                key = FeedOrigin.Key.KotlinBlog,
                title = "Kotlin Blog",
                description = "Latest news from the official Kotlin Blog",
                selected = true,
            ),
            FeedOrigin(
                key = FeedOrigin.Key.KotlinYouTubeChannel,
                title = "Kotlin YouTube",
                description = "Videos from the official Kotlin YouTube channel",
                selected = true,
            ),
            FeedOrigin(
                key = FeedOrigin.Key.TalkingKotlinPodcast,
                title = "Talking Kotlin",
                description = "Podcast on Kotlin and more by JetBrains",
                selected = false,
            ),
            FeedOrigin(
                key = FeedOrigin.Key.KotlinWeekly,
                title = "Kotlin Weekly",
                description = "Weekly community Kotlin newsletter",
                selected = true,
            ),
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                mockFeedOrigins.forEachIndexed { index, origin ->
                    FeedSourceCard(
                        origin = origin,
                        onToggle = {
                            mockFeedOrigins[index] = origin.copy(selected = !origin.selected)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun FeedSourceCard(
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
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val targetContentColor = if (origin.selected) {
        when (origin.key) {
            FeedOrigin.Key.KotlinBlog -> KSTheme.colorScheme.onAccent
            FeedOrigin.Key.KotlinYouTubeChannel -> KSTheme.colorScheme.onSurfaceYouTube
            FeedOrigin.Key.TalkingKotlinPodcast -> KSTheme.colorScheme.onSurfaceInverse
            FeedOrigin.Key.KotlinWeekly -> KSTheme.colorScheme.onAccent
        }
    } else {
        KSTheme.colorScheme.onBackground
    }

    val targetMutedContentColor = if (origin.selected) {
        when (origin.key) {
            FeedOrigin.Key.KotlinBlog -> KSTheme.colorScheme.onAccentMuted
            FeedOrigin.Key.KotlinYouTubeChannel -> KSTheme.colorScheme.onSurfaceYouTubeMuted
            FeedOrigin.Key.TalkingKotlinPodcast -> KSTheme.colorScheme.onSurfaceInverseMuted
            FeedOrigin.Key.KotlinWeekly -> KSTheme.colorScheme.onAccentMuted
        }
    } else {
        KSTheme.colorScheme.onBackgroundMuted
    }

    val contentColor by animateColorAsState(
        targetValue = targetContentColor,
        label = "contentColor"
    )
    val mutedContentColor by animateColorAsState(
        targetValue = targetMutedContentColor,
        label = "mutedContentColor"
    )

    val borderStroke = if (!origin.selected) {
        BorderStroke(1.dp, KSTheme.colorScheme.borderMuted)
    } else {
        null
    }

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
        border = borderStroke,
    ) {
        Box {
            val brandModifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = selectionProgress }
                .clip(RoundedCornerShape(16.dp))

            when (origin.key) {
                FeedOrigin.Key.KotlinBlog -> {
                    val meshColors = KSTheme.colorScheme.brandMeshGradient
                    val meshPainter = remember(KSTheme.colorScheme.isDark) {
                        val positions = arrayOf(
                            Offset(0.0f, 0.0f), Offset(0.5f, 0.0f), Offset(1.0f, 0.0f),
                            Offset(0.0f, 0.5f), Offset(0.55f, 0.45f), Offset(1.0f, 0.5f),
                            Offset(0.0f, 1.0f), Offset(0.5f, 1.0f), Offset(1.0f, 1.0f),
                        )
                        MeshGradientPainter(rows = 2, columns = 2, hasBicubicColor = true) {
                            for (row in 0..2) {
                                for (col in 0..2) {
                                    val idx = row * 3 + col
                                    setVertex(row, col, positions[idx], meshColors[idx])
                                }
                            }
                        }
                    }

                    Box(modifier = brandModifier.paint(meshPainter))
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
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = origin.title,
                        style = KSTheme.typography.titleLarge,
                        color = contentColor,
                    )
                    Text(
                        text = origin.description,
                        style = KSTheme.typography.titleSmall,
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

@Composable
@PreviewKStreamlined
private fun PreviewFeedSelectionSheet() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        FeedSelectionBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(
                initiallyExpanded = true,
                skipPartiallyExpanded = true,
            ),
        )
    }
}
