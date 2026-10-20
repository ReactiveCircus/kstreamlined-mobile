package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.color.LocalContentColor
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownListItem

@Composable
internal fun MarkdownBlocks(
    blocks: List<MarkdownBlock>,
    style: MarkdownStyle,
    modifier: Modifier = Modifier,
    listLevel: Int = 0,
    spacing: Dp = style.blockSpacing,
) {
    Column(modifier = modifier) {
        blocks.forEachIndexed { index, block ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(style.spacingBetween(blocks[index - 1], block, spacing)))
            }
            MarkdownBlock(block = block, style = style, listLevel = listLevel)
        }
    }
}

private fun MarkdownStyle.spacingBetween(previous: MarkdownBlock, current: MarkdownBlock, spacing: Dp): Dp = when {
    current is MarkdownBlock.Heading -> headingTopSpacing
    previous is MarkdownBlock.Heading -> headingBottomSpacing
    else -> spacing
}

@Composable
private fun MarkdownBlock(
    block: MarkdownBlock,
    style: MarkdownStyle,
    listLevel: Int,
) {
    when (block) {
        is MarkdownBlock.Paragraph -> {
            MarkdownText(inlines = block.inlines, textStyle = style.text, style = style)
        }

        is MarkdownBlock.Heading -> {
            MarkdownText(
                inlines = block.inlines,
                textStyle = style.heading(block.level),
                style = style,
                modifier = Modifier.semantics { heading() },
            )
        }

        is MarkdownBlock.BulletList -> {
            val marker = BulletMarkers[listLevel % BulletMarkers.size]
            MarkdownList(
                items = block.items,
                markers = List(block.items.size) { marker },
                isTight = block.isTight,
                style = style,
                listLevel = listLevel,
            )
        }

        is MarkdownBlock.OrderedList -> {
            MarkdownList(
                items = block.items,
                markers = List(block.items.size) { "${block.startNumber + it}." },
                isTight = block.isTight,
                style = style,
                listLevel = listLevel,
            )
        }

        is MarkdownBlock.CodeBlock -> {
            MarkdownCodeBlock(block = block, style = style.codeBlock)
        }

        is MarkdownBlock.BlockQuote -> {
            MarkdownBlockQuote(block = block, style = style, listLevel = listLevel)
        }

        MarkdownBlock.ThematicBreak -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(style.thematicBreak.thickness)
                    .background(style.thematicBreak.color),
            )
        }
    }
}

@Composable
private fun MarkdownText(
    inlines: List<MarkdownInline>,
    textStyle: TextStyle,
    style: MarkdownStyle,
    modifier: Modifier = Modifier,
) {
    val text = remember(inlines, style) { inlines.toAnnotatedString(style) }
    MarkdownText(text = text, textStyle = textStyle, inlineCodeStyle = style.inlineCode, modifier = modifier)
}

@Composable
private fun MarkdownText(
    text: AnnotatedString,
    textStyle: TextStyle,
    inlineCodeStyle: InlineCodeStyle,
    modifier: Modifier = Modifier,
) {
    if (text.hasInlineCode()) {
        var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
        Text(
            text = text,
            style = textStyle,
            onTextLayout = { textLayoutResult = it },
            modifier = modifier.inlineCodeBackground(
                textLayoutResult = { textLayoutResult },
                style = inlineCodeStyle,
            ),
        )
    } else {
        Text(text = text, style = textStyle, modifier = modifier)
    }
}

@Composable
private fun MarkdownList(
    items: List<MarkdownListItem>,
    markers: List<String>,
    isTight: Boolean,
    style: MarkdownStyle,
    listLevel: Int,
) {
    val markerStyle = style.list.marker
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    // align all markers (e.g. "9." and "10.") to the widest one
    val markerWidth = remember(markers, markerStyle, density) {
        with(density) {
            markers.maxOfOrNull { textMeasurer.measure(it, markerStyle).size.width }?.toDp() ?: 0.dp
        }
    }
    val itemSpacing = if (isTight) style.list.tightItemSpacing else style.blockSpacing
    Column(verticalArrangement = Arrangement.spacedBy(itemSpacing)) {
        items.forEachIndexed { index, item ->
            Row {
                Text(
                    text = markers[index],
                    style = markerStyle,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .width(markerWidth)
                        .alignByBaseline(),
                )
                Spacer(modifier = Modifier.width(style.list.markerSpacing))
                MarkdownBlocks(
                    blocks = item.blocks,
                    style = style,
                    listLevel = listLevel + 1,
                    spacing = itemSpacing,
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                )
            }
        }
    }
}

@Composable
private fun MarkdownCodeBlock(
    block: MarkdownBlock.CodeBlock,
    style: CodeBlockStyle,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(style.background, RoundedCornerShape(style.cornerRadius))
            .padding(vertical = style.padding),
    ) {
        if (block.language != null) {
            Text(
                text = block.language,
                style = style.languageLabel,
                modifier = Modifier
                    .padding(horizontal = style.padding)
                    .padding(bottom = 4.dp),
            )
        }
        Text(
            text = block.code,
            style = style.text,
            softWrap = false,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = style.padding),
        )
    }
}

@Composable
private fun MarkdownBlockQuote(
    block: MarkdownBlock.BlockQuote,
    style: MarkdownStyle,
    listLevel: Int,
) {
    val quoteStyle = style.blockQuote
    CompositionLocalProvider(LocalContentColor provides quoteStyle.textColor) {
        MarkdownBlocks(
            blocks = block.blocks,
            style = style,
            listLevel = listLevel,
            modifier = Modifier
                .drawBehind {
                    val barWidth = quoteStyle.barWidth.toPx()
                    val x = if (layoutDirection == LayoutDirection.Rtl) size.width - barWidth else 0f
                    drawRoundRect(
                        color = quoteStyle.barColor,
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, size.height),
                        cornerRadius = CornerRadius(quoteStyle.barWidth.toPx() / 2, quoteStyle.barWidth.toPx() / 2),
                    )
                }
                .padding(start = quoteStyle.barWidth + quoteStyle.barSpacing),
        )
    }
}

private val BulletMarkers = listOf("•", "▪", "–")
