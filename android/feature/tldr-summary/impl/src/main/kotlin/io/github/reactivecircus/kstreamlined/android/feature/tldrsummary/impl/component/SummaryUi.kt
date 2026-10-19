package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.InlineCodeTag

@Composable
internal fun SummaryUi(
    summary: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    val inlineCodeColor = KSTheme.colorScheme.surface
    var inlineCodeBounds by remember { mutableStateOf(emptyList<Rect>()) }
    Text(
        text = summary,
        style = KSTheme.typography.bodyLarge.copy(letterSpacing = 0.1.sp),
        color = KSTheme.colorScheme.onBackground,
        onTextLayout = { inlineCodeBounds = it.inlineCodeBounds() },
        modifier = modifier.drawBehind {
            val cornerRadius = CornerRadius(InlineCodeCornerRadius.toPx())
            inlineCodeBounds.forEach { bounds ->
                drawRoundRect(
                    color = inlineCodeColor,
                    topLeft = bounds.topLeft,
                    size = bounds.size,
                    cornerRadius = cornerRadius,
                )
            }
        },
    )
}

private fun TextLayoutResult.inlineCodeBounds(): List<Rect> = with(layoutInput.density) {
    val fontSize = layoutInput.style.fontSize.toPx()
    val horizontalPadding = InlineCodeHorizontalPadding.toPx()
    val verticalPadding = InlineCodeVerticalPadding.toPx()
    layoutInput.text.getStringAnnotations(InlineCodeTag, 0, layoutInput.text.length).flatMap { range ->
        (getLineForOffset(range.start)..getLineForOffset(range.end - 1)).mapNotNull { line ->
            val start = maxOf(range.start, getLineStart(line))
            val end = minOf(range.end, getLineEnd(line, visibleEnd = true))
            if (start >= end) return@mapNotNull null
            val baseline = getLineBaseline(line)
            Rect(
                left = getBoundingBox(start).left - horizontalPadding,
                top = baseline - fontSize * GlyphTopEm - verticalPadding,
                right = getBoundingBox(end - 1).right + horizontalPadding,
                bottom = baseline + fontSize * GlyphBottomEm + verticalPadding,
            )
        }
    }
}

private val InlineCodeHorizontalPadding = 4.dp
private val InlineCodeVerticalPadding = 2.dp
private val InlineCodeCornerRadius = 4.dp
private const val GlyphTopEm = 0.84f
private const val GlyphBottomEm = 0.18f
