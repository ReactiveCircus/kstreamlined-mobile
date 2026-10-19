package io.github.reactivecircus.kstreamlined.android.core.ui.markdown.internal

/**
 * Parser-agnostic representation of a Markdown document.
 */
internal data class MarkdownDocument(
    val blocks: List<MarkdownBlock>,
)

internal sealed interface MarkdownBlock {
    data class Paragraph(val inlines: List<MarkdownInline>) : MarkdownBlock

    data class Heading(val level: Int, val inlines: List<MarkdownInline>) : MarkdownBlock

    data class BulletList(
        val items: List<MarkdownListItem>,
        val isTight: Boolean,
    ) : MarkdownBlock

    data class OrderedList(
        val startNumber: Int,
        val items: List<MarkdownListItem>,
        val isTight: Boolean,
    ) : MarkdownBlock

    data class CodeBlock(val code: String, val language: String?) : MarkdownBlock

    data class BlockQuote(val blocks: List<MarkdownBlock>) : MarkdownBlock

    data object ThematicBreak : MarkdownBlock
}

internal data class MarkdownListItem(
    val blocks: List<MarkdownBlock>,
)

internal sealed interface MarkdownInline {
    data class Text(val text: String) : MarkdownInline

    data class Code(val code: String) : MarkdownInline

    data class Emphasis(val inlines: List<MarkdownInline>) : MarkdownInline

    data class Strong(val inlines: List<MarkdownInline>) : MarkdownInline

    data class Link(
        val destination: String,
        val title: String?,
        val inlines: List<MarkdownInline>,
    ) : MarkdownInline

    data object SoftBreak : MarkdownInline

    data object HardBreak : MarkdownInline
}
