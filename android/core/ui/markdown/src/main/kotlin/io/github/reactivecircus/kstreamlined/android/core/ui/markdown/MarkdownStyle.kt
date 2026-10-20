package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.color.KSColorScheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.typography.KSTypography

@Immutable
internal data class MarkdownStyle(
    val text: TextStyle,
    val headings: List<TextStyle>,
    val strong: SpanStyle,
    val emphasis: SpanStyle,
    val link: TextLinkStyles,
    val inlineCode: InlineCodeStyle,
    val codeBlock: CodeBlockStyle,
    val list: ListStyle,
    val blockQuote: BlockQuoteStyle,
    val thematicBreak: ThematicBreakStyle,
    val blockSpacing: Dp,
    val headingTopSpacing: Dp,
    val headingBottomSpacing: Dp,
) {
    fun heading(level: Int): TextStyle = headings[(level - 1).coerceIn(0, headings.lastIndex)]
}

@Immutable
internal data class InlineCodeStyle(
    val spanStyle: SpanStyle,
    val background: Color,
    val cornerRadius: Dp,
    val horizontalPadding: TextUnit,
    val verticalPadding: TextUnit,
)

@Immutable
internal data class CodeBlockStyle(
    val text: TextStyle,
    val languageLabel: TextStyle,
    val background: Color,
    val cornerRadius: Dp,
    val padding: Dp,
)

@Immutable
internal data class ListStyle(
    val marker: TextStyle,
    val markerSpacing: Dp,
    val tightItemSpacing: Dp,
)

@Immutable
internal data class BlockQuoteStyle(
    val textColor: Color,
    val barColor: Color,
    val barWidth: Dp,
    val barSpacing: Dp,
)

@Immutable
internal data class ThematicBreakStyle(
    val color: Color,
    val thickness: Dp,
)

@Composable
internal fun rememberDefaultMarkdownStyle(): MarkdownStyle {
    val typography = KSTheme.typography
    val colorScheme = KSTheme.colorScheme
    return remember(typography, colorScheme) { defaultMarkdownStyle(typography, colorScheme) }
}

private fun defaultMarkdownStyle(typography: KSTypography, colorScheme: KSColorScheme): MarkdownStyle {
    val text = typography.bodyLarge.copy(letterSpacing = 0.1.sp)
    return MarkdownStyle(
        text = text,
        headings = listOf(typography.titleLarge, typography.titleMedium, text.copy(fontWeight = FontWeight.Bold)),
        strong = SpanStyle(fontWeight = FontWeight.Bold),
        emphasis = SpanStyle(fontStyle = FontStyle.Italic),
        link = TextLinkStyles(
            style = SpanStyle(
                color = colorScheme.accent,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        inlineCode = InlineCodeStyle(
            spanStyle = SpanStyle(),
            background = colorScheme.surface,
            cornerRadius = 4.dp,
            horizontalPadding = 0.25.em,
            verticalPadding = 0.18.em,
        ),
        codeBlock = CodeBlockStyle(
            text = typography.bodyMedium,
            languageLabel = typography.labelSmall.copy(color = colorScheme.onBackgroundMuted),
            background = colorScheme.surface,
            cornerRadius = 8.dp,
            padding = 12.dp,
        ),
        list = ListStyle(
            marker = text.copy(color = colorScheme.onBackgroundMuted),
            markerSpacing = 8.dp,
            tightItemSpacing = 4.dp,
        ),
        blockQuote = BlockQuoteStyle(
            textColor = colorScheme.onBackgroundMuted,
            barColor = colorScheme.borderMuted,
            barWidth = 4.dp,
            barSpacing = 12.dp,
        ),
        thematicBreak = ThematicBreakStyle(
            color = colorScheme.borderMuted,
            thickness = 1.dp,
        ),
        blockSpacing = 12.dp,
        headingTopSpacing = 20.dp,
        headingBottomSpacing = 4.dp,
    )
}
