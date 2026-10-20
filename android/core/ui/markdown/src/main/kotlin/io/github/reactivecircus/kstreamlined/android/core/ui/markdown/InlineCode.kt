package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.em
import kotlin.math.max
import kotlin.math.min

/**
 * Appends [code] wrapped in no-break spaces which act as horizontal padding for the background,
 * so that the background never overlaps adjacent characters and stays attached to the code across line breaks.
 */
internal fun AnnotatedString.Builder.appendInlineCode(code: String, style: InlineCodeStyle) {
    val paddingStyle = SpanStyle(fontSize = (style.horizontalPadding.value / NoBreakSpaceAdvanceEm).em)
    withAnnotation(tag = InlineCodeTag, annotation = code) {
        withStyle(paddingStyle) { append(NoBreakSpace) }
        withStyle(style.spanStyle) { append(code) }
        withStyle(paddingStyle) { append(NoBreakSpace) }
    }
}

/**
 * Draws rounded backgrounds behind inline code ranges appended with [appendInlineCode].
 */
internal fun Modifier.inlineCodeBackground(
    textLayoutResult: () -> TextLayoutResult?,
    style: InlineCodeStyle,
): Modifier = drawWithCache {
    val path = textLayoutResult()?.inlineCodeBackgroundPath(style, density = this)
    onDrawBehind {
        if (path != null) drawPath(path, style.background)
    }
}

internal fun AnnotatedString.hasInlineCode(): Boolean = hasStringAnnotations(InlineCodeTag, 0, length)

private fun TextLayoutResult.inlineCodeBackgroundPath(style: InlineCodeStyle, density: Density): Path {
    val path = Path()
    val fontSize = with(density) { layoutInput.style.fontSize.toPx() }
    val verticalPadding = fontSize * style.verticalPadding.value
    val cornerRadius = CornerRadius(with(density) { style.cornerRadius.toPx() })
    val text = layoutInput.text
    text.getStringAnnotations(InlineCodeTag, 0, text.length).forEach { range ->
        // one rect per line the code wraps across
        val rects = (getLineForOffset(range.start)..getLineForOffset(range.end - 1)).mapNotNull { line ->
            val start = max(range.start, getLineStart(line))
            val end = min(range.end, getLineEnd(line, visibleEnd = true))
            if (start >= end) return@mapNotNull null
            val startBox = getBoundingBox(start)
            val endBox = getBoundingBox(end - 1)
            val baseline = getLineBaseline(line)
            Rect(
                left = min(startBox.left, endBox.left),
                top = baseline - fontSize * GlyphAscentEm - verticalPadding,
                right = max(startBox.right, endBox.right),
                bottom = baseline + fontSize * GlyphDescentEm + verticalPadding,
            )
        }
        // only round the outer corners when wrapped across lines (assumes LTR)
        rects.forEachIndexed { index, rect ->
            val startCorner = if (index == 0) cornerRadius else CornerRadius.Zero
            val endCorner = if (index == rects.lastIndex) cornerRadius else CornerRadius.Zero
            path.addRoundRect(
                RoundRect(
                    rect = rect,
                    topLeft = startCorner,
                    bottomLeft = startCorner,
                    topRight = endCorner,
                    bottomRight = endCorner,
                ),
            )
        }
    }
    return path
}

private const val InlineCodeTag = "markdown:inline-code"
private const val NoBreakSpace = '\u00A0'

// JetBrains Mono metrics (units per em = 1000), used until Compose exposes font metrics.
// Advance width of all glyphs including U+00A0.
private const val NoBreakSpaceAdvanceEm = 0.6f

// Top of brackets / accents.
private const val GlyphAscentEm = 0.84f

// Bottom of descenders.
private const val GlyphDescentEm = 0.18f
