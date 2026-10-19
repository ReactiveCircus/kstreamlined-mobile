package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined

@Composable
internal fun LoadingSkeletonUi(
    modifier: Modifier = Modifier,
) {
    val lineColor = KSTheme.colorScheme.accent.copy(alpha = LineAlpha)
    val emphasisColor = KSTheme.colorScheme.accent.copy(alpha = EmphasisAlpha)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SkeletonShape(color = lineColor, modifier = Modifier.size(width = 84.dp, height = 12.dp))
            SkeletonShape(color = lineColor, modifier = Modifier.size(width = 100.dp, height = 24.dp))
            SkeletonShape(color = lineColor, modifier = Modifier.size(width = 84.dp, height = 12.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        ParagraphLineFractions.forEach { fraction ->
            SkeletonLine(fraction = fraction, barHeight = LineBarHeight, color = lineColor)
        }
        Sections.forEach { (headingFraction = first, bulletFractions = second) ->
            Spacer(modifier = Modifier.height(SectionSpacing))
            SkeletonLine(fraction = headingFraction, barHeight = HeadingBarHeight, color = emphasisColor)
            bulletFractions.forEach { fraction ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SkeletonShape(color = emphasisColor, modifier = Modifier.size(BulletSize))
                    SkeletonLine(fraction = fraction, barHeight = LineBarHeight, color = lineColor)
                }
            }
        }
    }
}

@Composable
private fun SkeletonLine(
    fraction: Float,
    barHeight: Dp,
    color: Color,
    modifier: Modifier = Modifier,
) {
    SkeletonShape(
        color = color,
        modifier = modifier
            .padding(vertical = (LineHeight - barHeight) / 2)
            .fillMaxWidth(fraction)
            .height(barHeight),
    )
}

@Composable
private fun SkeletonShape(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(color = color, shape = CircleShape))
}

private val LineHeight = 24.dp
private val LineBarHeight = 12.dp
private val HeadingBarHeight = 16.dp
private val SectionSpacing = 12.dp
private val BulletSize = 8.dp
private const val LineAlpha = 0.12f
private const val EmphasisAlpha = 0.18f
private val ParagraphLineFractions = listOf(0.96f, 0.88f, 0.58f)
private val Sections = listOf(
    0.42f to listOf(0.8f, 0.92f, 0.66f),
    0.34f to listOf(0.86f, 0.72f),
    0.38f to listOf(0.78f, 0.9f, 0.6f),
)

@Composable
@PreviewKStreamlined
private fun PreviewLoadingSkeletonUi() {
    LoadingSkeletonUi(modifier = Modifier.padding(24.dp))
}
