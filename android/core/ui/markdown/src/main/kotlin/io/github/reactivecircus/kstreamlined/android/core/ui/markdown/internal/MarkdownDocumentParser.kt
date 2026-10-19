package io.github.reactivecircus.kstreamlined.android.core.ui.markdown.internal

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.impl.ListCompositeNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.CancellationToken
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser

/**
 * Parses CommonMark [markdown] into a [MarkdownDocument].
 */
internal fun parseMarkdown(markdown: String): MarkdownDocument {
    // the parser requires LF line endings
    val text: CharSequence = markdown.replace("\r\n", "\n").replace('\r', '\n')
    val root = MarkdownParser(
        flavour = CommonMarkFlavourDescriptor(),
        assertionsEnabled = false,
        cancellationToken = CancellationToken.NonCancellable,
    ).buildMarkdownTreeFromString(text)
    val inlineMapper = InlineMapper(text, LinkMap.buildLinkMap(root, text))
    return MarkdownDocument(BlockMapper(text, inlineMapper).mapBlocks(root.children))
}

private class BlockMapper(
    private val text: CharSequence,
    private val inlineMapper: InlineMapper,
) {
    fun mapBlocks(nodes: List<ASTNode>): List<MarkdownBlock> = nodes.mapNotNull(::mapBlock)

    @Suppress("CyclomaticComplexMethod")
    private fun mapBlock(node: ASTNode): MarkdownBlock? = when (val type = node.type) {
        MarkdownElementTypes.PARAGRAPH -> {
            inlineMapper.mapInlines(node.children, trim = true)
                .takeIf { it.isNotEmpty() }
                ?.let(MarkdownBlock::Paragraph)
        }

        in AtxHeadingLevels -> {
            mapHeading(AtxHeadingLevels.getValue(type), node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT))
        }

        MarkdownElementTypes.SETEXT_1 -> {
            mapHeading(1, node.findChildOfType(MarkdownTokenTypes.SETEXT_CONTENT))
        }

        MarkdownElementTypes.SETEXT_2 -> {
            mapHeading(2, node.findChildOfType(MarkdownTokenTypes.SETEXT_CONTENT))
        }

        MarkdownElementTypes.UNORDERED_LIST -> {
            MarkdownBlock.BulletList(
                items = mapListItems(node),
                isTight = !node.isLooseList(),
            )
        }

        MarkdownElementTypes.ORDERED_LIST -> {
            MarkdownBlock.OrderedList(
                startNumber = node.orderedListStartNumber(text),
                items = mapListItems(node),
                isTight = !node.isLooseList(),
            )
        }

        MarkdownElementTypes.CODE_FENCE -> mapCodeFence(node)

        MarkdownElementTypes.CODE_BLOCK -> mapIndentedCodeBlock(node)

        MarkdownElementTypes.BLOCK_QUOTE -> MarkdownBlock.BlockQuote(mapBlocks(node.children))

        MarkdownTokenTypes.HORIZONTAL_RULE -> MarkdownBlock.ThematicBreak

        MarkdownElementTypes.HTML_BLOCK -> mapHtmlBlock(node)

        else -> null
    }

    private fun mapHeading(level: Int, content: ASTNode?): MarkdownBlock.Heading? {
        val inlines = content?.let { inlineMapper.mapInlines(it.children, trim = true) }.orEmpty()
        return if (inlines.isEmpty()) null else MarkdownBlock.Heading(level, inlines)
    }

    private fun mapListItems(list: ASTNode): List<MarkdownListItem> = list.children
        .filter { it.type == MarkdownElementTypes.LIST_ITEM }
        .map { MarkdownListItem(mapBlocks(it.children)) }

    private fun mapCodeFence(node: ASTNode): MarkdownBlock.CodeBlock {
        val children = node.children
        // indentation of the opening fence is removed from each content line
        val fenceIndent = children.firstOrNull()?.rawText(text)?.takeWhile { it == ' ' }?.length ?: 0
        val language = node.findChildOfType(MarkdownTokenTypes.FENCE_LANG)
            ?.let { decodeEntitiesAndEscapes(it.rawText(text).trim()) }
            ?.takeWhile { !it.isWhitespace() }
            ?.takeIf { it.isNotEmpty() }
        val contentStart = children.indexOfFirst { it.type == MarkdownTokenTypes.EOL } + 1
        val code = buildString {
            if (contentStart == 0) return@buildString
            children.subList(contentStart, children.size).forEach { child ->
                when (child.type) {
                    MarkdownTokenTypes.CODE_FENCE_CONTENT -> append(child.rawText(text).trimIndent(fenceIndent))
                    MarkdownTokenTypes.EOL -> append('\n')
                }
            }
        }
        return MarkdownBlock.CodeBlock(code = code.removeSuffix("\n"), language = language)
    }

    private fun mapIndentedCodeBlock(node: ASTNode): MarkdownBlock.CodeBlock {
        val code = buildString {
            node.children.forEach { child ->
                when (child.type) {
                    MarkdownTokenTypes.CODE_LINE -> append(child.rawText(text).trimIndent(IndentedCodeBlockIndent))
                    MarkdownTokenTypes.EOL -> append('\n')
                }
            }
        }
        return MarkdownBlock.CodeBlock(code = code.trimEnd('\n'), language = null)
    }

    private fun mapHtmlBlock(node: ASTNode): MarkdownBlock.Paragraph? {
        val builder = InlinesBuilder()
        node.children.forEach { child ->
            when (child.type) {
                MarkdownTokenTypes.HTML_BLOCK_CONTENT -> builder.text(child.rawText(text))
                MarkdownTokenTypes.EOL -> builder.hardBreak()
            }
        }
        return builder.build(trim = true).takeIf { it.isNotEmpty() }?.let(MarkdownBlock::Paragraph)
    }
}

internal fun ASTNode.rawText(text: CharSequence): String = getTextInNode(text).toString()

private fun ASTNode.isLooseList(): Boolean = (this as? ListCompositeNode)?.loose == true

private fun ASTNode.orderedListStartNumber(text: CharSequence): Int = children
    .firstOrNull { it.type == MarkdownElementTypes.LIST_ITEM }
    ?.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)
    ?.rawText(text)
    ?.trim()
    ?.dropLast(1) // delimiter: '.' or ')'
    ?.toIntOrNull()
    ?: 1

/**
 * Removes up to [indent] columns of leading whitespace, expanding tabs to the next multiple of 4.
 */
private fun String.trimIndent(indent: Int): String {
    var column = 0
    var offset = 0
    while (offset < length && column < indent) {
        when (this[offset]) {
            ' ' -> column++
            '\t' -> column += TabWidth - column % TabWidth
            else -> break
        }
        offset++
    }
    val excess = (column - indent).coerceAtLeast(0)
    return " ".repeat(excess) + substring(offset)
}

private val AtxHeadingLevels = mapOf(
    MarkdownElementTypes.ATX_1 to 1,
    MarkdownElementTypes.ATX_2 to 2,
    MarkdownElementTypes.ATX_3 to 3,
    MarkdownElementTypes.ATX_4 to 4,
    MarkdownElementTypes.ATX_5 to 5,
    MarkdownElementTypes.ATX_6 to 6,
)

private const val IndentedCodeBlockIndent = 4
private const val TabWidth = 4
