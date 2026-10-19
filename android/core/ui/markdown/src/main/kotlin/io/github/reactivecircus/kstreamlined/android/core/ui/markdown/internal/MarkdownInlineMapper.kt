package io.github.reactivecircus.kstreamlined.android.core.ui.markdown.internal

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.parser.LinkMap

/**
 * Maps inline nodes of a JetBrains/markdown AST to [MarkdownInline]s.
 */
internal class InlineMapper(
    private val text: CharSequence,
    private val linkMap: LinkMap,
) {
    fun mapInlines(nodes: List<ASTNode>, trim: Boolean = false): List<MarkdownInline> {
        val builder = InlinesBuilder()
        var index = 0
        while (index < nodes.size) {
            val email = nodes.emailAutolinkAt(index)
            if (email != null) {
                builder.add(
                    MarkdownInline.Link(
                        destination = "mailto:$email",
                        title = null,
                        inlines = listOf(MarkdownInline.Text(email)),
                    ),
                )
                index += EmailAutolinkTokenCount
            } else {
                mapInline(nodes[index], builder)
                index++
            }
        }
        return builder.build(trim)
    }

    @Suppress("CyclomaticComplexMethod")
    private fun mapInline(node: ASTNode, builder: InlinesBuilder) {
        when (node.type) {
            MarkdownTokenTypes.TEXT -> builder.text(decodeEntitiesAndEscapes(node.rawText(text)))

            MarkdownTokenTypes.WHITE_SPACE -> builder.whitespace(node.rawText(text))

            MarkdownTokenTypes.EOL -> builder.softBreak()

            MarkdownTokenTypes.HARD_LINE_BREAK -> builder.hardBreak()

            MarkdownTokenTypes.BLOCK_QUOTE -> Unit

            MarkdownTokenTypes.HTML_TAG -> builder.text(node.rawText(text))

            MarkdownElementTypes.EMPH -> {
                builder.add(MarkdownInline.Emphasis(mapInlines(node.children.innerChildren(EmphasisDelimiterCount))))
            }

            MarkdownElementTypes.STRONG -> {
                builder.add(MarkdownInline.Strong(mapInlines(node.children.innerChildren(StrongDelimiterCount))))
            }

            MarkdownElementTypes.CODE_SPAN -> builder.add(MarkdownInline.Code(node.codeSpanText()))

            MarkdownElementTypes.INLINE_LINK -> mapInlineLink(node, builder)

            MarkdownElementTypes.FULL_REFERENCE_LINK,
            MarkdownElementTypes.SHORT_REFERENCE_LINK,
            -> mapReferenceLink(node, builder)

            MarkdownElementTypes.AUTOLINK -> mapAutolink(node, builder)

            MarkdownElementTypes.IMAGE -> mapImage(node, builder)

            else -> mapFallback(node, builder)
        }
    }

    /**
     * Keeps the source text of unsupported or unresolved nodes, e.g. `[label]` without a link definition.
     */
    private fun mapFallback(node: ASTNode, builder: InlinesBuilder) {
        if (node.children.isEmpty()) {
            builder.text(decodeEntitiesAndEscapes(node.rawText(text)))
        } else {
            node.children.forEach { mapInline(it, builder) }
        }
    }

    private fun mapInlineLink(node: ASTNode, builder: InlinesBuilder) {
        val label = node.findChildOfType(MarkdownElementTypes.LINK_TEXT)
            ?: return mapFallback(node, builder)
        builder.link(
            destination = node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                ?.let { normalizeDestination(it.rawText(text)) }
                .orEmpty(),
            title = node.findChildOfType(MarkdownElementTypes.LINK_TITLE)?.let { normalizeTitle(it.rawText(text)) },
            inlines = mapInlines(label.children.innerChildren(1)),
        )
    }

    private fun mapReferenceLink(node: ASTNode, builder: InlinesBuilder) {
        val label = node.findChildOfType(MarkdownElementTypes.LINK_LABEL)
            ?: return mapFallback(node, builder)
        val definition = linkMap.getLinkInfo(label.rawText(text))?.node
            ?: return mapFallback(node, builder)
        val linkText = node.findChildOfType(MarkdownElementTypes.LINK_TEXT) ?: label
        builder.link(
            destination = definition.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                ?.let { normalizeDestination(it.rawText(text)) }
                .orEmpty(),
            title = definition.findChildOfType(MarkdownElementTypes.LINK_TITLE)
                ?.let { normalizeTitle(it.rawText(text)) },
            inlines = mapInlines(linkText.children.innerChildren(1)),
        )
    }

    private fun mapAutolink(node: ASTNode, builder: InlinesBuilder) {
        val url = node.findChildOfType(MarkdownTokenTypes.AUTOLINK)
            ?.let { decodeEntitiesAndEscapes(it.rawText(text), processEscapes = false) }
            ?: return mapFallback(node, builder)
        builder.link(destination = url, title = null, inlines = listOf(MarkdownInline.Text(url)))
    }

    private fun mapImage(node: ASTNode, builder: InlinesBuilder) {
        val link = node.findChildOfType(MarkdownElementTypes.INLINE_LINK)
            ?: node.findChildOfType(MarkdownElementTypes.FULL_REFERENCE_LINK)
            ?: node.findChildOfType(MarkdownElementTypes.SHORT_REFERENCE_LINK)
        val altText = link?.let {
            it.findChildOfType(MarkdownElementTypes.LINK_TEXT) ?: it.findChildOfType(MarkdownElementTypes.LINK_LABEL)
        } ?: return mapFallback(node, builder)
        builder.text(mapInlines(altText.children.innerChildren(1)).plainText())
    }

    private fun ASTNode.codeSpanText(): String {
        var atLineStart = false
        val code = buildString {
            children.innerChildren(1).forEach { child ->
                when (child.type) {
                    // line endings are converted to spaces
                    MarkdownTokenTypes.EOL -> {
                        append(' ')
                        atLineStart = true
                    }

                    MarkdownTokenTypes.BLOCK_QUOTE -> Unit

                    MarkdownTokenTypes.WHITE_SPACE -> if (!atLineStart) append(child.rawText(text))

                    else -> {
                        append(child.rawText(text))
                        atLineStart = false
                    }
                }
            }
        }
        return code.stripCodeSpanPadding()
    }

    private fun List<ASTNode>.emailAutolinkAt(index: Int): String? {
        if (index + EmailAutolinkTokenCount > size) return null
        val isEmailAutolink = this[index].type == MarkdownTokenTypes.LT &&
            this[index + 1].type == MarkdownTokenTypes.EMAIL_AUTOLINK &&
            this[index + 2].type == MarkdownTokenTypes.GT
        return if (isEmailAutolink) this[index + 1].rawText(text) else null
    }
}

/**
 * Accumulates inlines, merging adjacent text and applying CommonMark whitespace rules around line breaks.
 */
internal class InlinesBuilder {
    private val inlines = mutableListOf<MarkdownInline>()
    private val pendingText = StringBuilder()
    private var atLineStart = false

    fun text(value: String) {
        if (value.isEmpty()) return
        pendingText.append(value)
        atLineStart = false
    }

    /**
     * Leading whitespace of continuation lines is ignored.
     */
    fun whitespace(value: String) {
        if (!atLineStart) text(value)
    }

    fun softBreak() {
        // a line ending right after a hard break is part of the hard break
        if (pendingText.isEmpty() && inlines.lastOrNull() == MarkdownInline.HardBreak) {
            atLineStart = true
            return
        }
        addBreak(MarkdownInline.SoftBreak)
    }

    fun hardBreak() = addBreak(MarkdownInline.HardBreak)

    fun add(inline: MarkdownInline) {
        if (inline is MarkdownInline.Text) {
            text(inline.text)
        } else {
            flushText()
            inlines += inline
            atLineStart = false
        }
    }

    fun link(destination: String, title: String?, inlines: List<MarkdownInline>) {
        if (destination.isBlank()) {
            inlines.forEach(::add)
        } else {
            add(MarkdownInline.Link(destination = destination, title = title, inlines = inlines))
        }
    }

    fun build(trim: Boolean): List<MarkdownInline> {
        flushText()
        if (!trim) return inlines.toList()
        val trimmed = inlines
            .dropWhile { it.isLineBreak() }
            .dropLastWhile { it.isLineBreak() }
            .toMutableList()
        trimmed.trimTextAt(0) { it.trimStart(' ', '\t') }
        trimmed.trimTextAt(trimmed.lastIndex) { it.trimEnd(' ', '\t') }
        return trimmed
    }

    private fun addBreak(lineBreak: MarkdownInline) {
        // trailing spaces before a line ending are ignored
        pendingText.setLength(pendingText.trimEnd(' ').length)
        flushText()
        inlines += lineBreak
        atLineStart = true
    }

    private fun flushText() {
        if (pendingText.isEmpty()) return
        inlines += MarkdownInline.Text(pendingText.toString())
        pendingText.clear()
    }
}

private fun MarkdownInline.isLineBreak(): Boolean =
    this == MarkdownInline.SoftBreak || this == MarkdownInline.HardBreak

private fun MutableList<MarkdownInline>.trimTextAt(index: Int, trim: (String) -> String) {
    val inline = getOrNull(index) as? MarkdownInline.Text ?: return
    val trimmedText = trim(inline.text)
    if (trimmedText.isEmpty()) removeAt(index) else set(index, MarkdownInline.Text(trimmedText))
}

private fun List<MarkdownInline>.plainText(): String = joinToString(separator = "") { inline ->
    when (inline) {
        is MarkdownInline.Text -> inline.text
        is MarkdownInline.Code -> inline.code
        is MarkdownInline.Emphasis -> inline.inlines.plainText()
        is MarkdownInline.Strong -> inline.inlines.plainText()
        is MarkdownInline.Link -> inline.inlines.plainText()
        MarkdownInline.SoftBreak, MarkdownInline.HardBreak -> " "
    }
}

/**
 * A single leading and trailing space is stripped if both are present and the content isn't all spaces.
 */
private fun String.stripCodeSpanPadding(): String {
    val isPadded = length >= 2 && first() == ' ' && last() == ' '
    return if (isPadded && isNotBlank()) substring(1, length - 1) else this
}

/**
 * Children without [count] delimiter nodes on each side.
 */
private fun List<ASTNode>.innerChildren(count: Int): List<ASTNode> =
    if (size >= count * 2) subList(count, size - count) else emptyList()

private fun normalizeDestination(destination: String): String =
    decodeEntitiesAndEscapes(destination.trim().removeSurrounding("<", ">"))

private fun normalizeTitle(title: String): String {
    val isQuoted = title.length >= 2 && (title.first() to title.last()) in QuotePairs
    val unquoted = if (isQuoted) title.substring(1, title.length - 1) else title
    return decodeEntitiesAndEscapes(unquoted)
}

private val QuotePairs = listOf('"' to '"', '\'' to '\'', '(' to ')')

private const val EmphasisDelimiterCount = 1
private const val StrongDelimiterCount = 2
private const val EmailAutolinkTokenCount = 3
