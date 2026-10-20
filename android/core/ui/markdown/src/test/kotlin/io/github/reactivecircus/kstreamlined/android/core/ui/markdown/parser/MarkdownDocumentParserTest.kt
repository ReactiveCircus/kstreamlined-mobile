package io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser

import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.BlockQuote
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.BulletList
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.CodeBlock
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.Heading
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.OrderedList
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.Paragraph
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownBlock.ThematicBreak
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.Code
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.Emphasis
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.HardBreak
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.Link
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.SoftBreak
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.Strong
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline.Text
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownDocumentParserTest {

    @Test
    fun mixedBlockAndInlineElements() {
        val markdown = """
            |Kotlin 2.4.20 is a tooling release focused on **multiplatform interop**, smaller *Wasm* binaries and a
            |faster compiler CLI.
            |
            |## Standard library
            |
            |- Experimental `allDistinct()` and `allEqual()` check collection elements.
            |- New overloads for `kotlin.test` assertion functions.
            |
            |## Multiplatform
            |
            |1. Swift export maps **sealed classes** to Swift enums:
            |
            |   ```kotlin
            |   sealed interface Shape {
            |       data object Unknown : Shape
            |   }
            |   ```
            |
            |2. Generated `Package.swift` for SwiftPM dependencies.
            |
            |See [What's new in Kotlin 2.4.20](https://kotlinlang.org/docs/whatsnew2420.html) for details.
        """.trimMargin()

        assertEquals(
            document(
                paragraph(
                    Text("Kotlin 2.4.20 is a tooling release focused on "),
                    Strong(listOf(Text("multiplatform interop"))),
                    Text(", smaller "),
                    Emphasis(listOf(Text("Wasm"))),
                    Text(" binaries and a"),
                    SoftBreak,
                    Text("faster compiler CLI."),
                ),
                Heading(2, listOf(Text("Standard library"))),
                BulletList(
                    items = listOf(
                        item(
                            paragraph(
                                Text("Experimental "),
                                Code("allDistinct()"),
                                Text(" and "),
                                Code("allEqual()"),
                                Text(" check collection elements."),
                            ),
                        ),
                        item(
                            paragraph(
                                Text("New overloads for "),
                                Code("kotlin.test"),
                                Text(" assertion functions."),
                            ),
                        ),
                    ),
                    isTight = true,
                ),
                Heading(2, listOf(Text("Multiplatform"))),
                OrderedList(
                    startNumber = 1,
                    items = listOf(
                        item(
                            paragraph(
                                Text("Swift export maps "),
                                Strong(listOf(Text("sealed classes"))),
                                Text(" to Swift enums:"),
                            ),
                            CodeBlock(
                                code = "sealed interface Shape {\n    data object Unknown : Shape\n}",
                                language = "kotlin",
                            ),
                        ),
                        item(
                            paragraph(
                                Text("Generated "),
                                Code("Package.swift"),
                                Text(" for SwiftPM dependencies."),
                            ),
                        ),
                    ),
                    isTight = false,
                ),
                paragraph(
                    Text("See "),
                    Link(
                        destination = "https://kotlinlang.org/docs/whatsnew2420.html",
                        title = null,
                        inlines = listOf(Text("What's new in Kotlin 2.4.20")),
                    ),
                    Text(" for details."),
                ),
            ),
            parseMarkdown(markdown),
        )
    }

    @Test
    fun emptyInput() {
        assertEquals(document(), parseMarkdown(""))
        assertEquals(document(), parseMarkdown("  \n\n "))
    }

    @Test
    fun crlfLineEndings() {
        assertEquals(
            document(
                paragraph(Text("a"), SoftBreak, Text("b")),
                paragraph(Text("c")),
            ),
            parseMarkdown("a\r\nb\r\n\r\nc\r"),
        )
    }

    @Test
    fun paragraphWhitespace() {
        assertEquals(
            document(paragraph(Text("one"), SoftBreak, Text("two"))),
            parseMarkdown("  one \n     two  "),
        )
    }

    @Test
    fun hardLineBreaks() {
        assertEquals(
            document(paragraph(Text("a"), HardBreak, Text("b"), HardBreak, Text("c"))),
            parseMarkdown("a  \nb\\\nc"),
        )
    }

    @Test
    fun emphasis() {
        assertEquals(
            document(
                paragraph(
                    Emphasis(listOf(Strong(listOf(Text("both"))))),
                    Text(" "),
                    Emphasis(listOf(Text("underscore"))),
                    Text(" "),
                    Strong(listOf(Text("bold "), Emphasis(listOf(Text("nested"))))),
                ),
            ),
            parseMarkdown("***both*** _underscore_ **bold *nested***"),
        )
    }

    @Test
    fun unmatchedDelimitersAreLiteral() {
        assertEquals(
            document(paragraph(Text("2 * 3 and a ` tick"))),
            parseMarkdown("2 * 3 and a ` tick"),
        )
    }

    @Test
    fun escapesAndEntities() {
        assertEquals(
            document(paragraph(Text("*not emphasis* & <tag> © ©"))),
            parseMarkdown("\\*not emphasis\\* &amp; &lt;tag&gt; &copy; &#169;"),
        )
    }

    @Test
    fun rawSpecialCharactersArePreserved() {
        assertEquals(
            document(paragraph(Text("R&D \"quoted\" a < b > c &unknown; 😀 \uFFFD \\d"))),
            parseMarkdown("R&D \"quoted\" a < b > c &unknown; &#x1F600; &#0; \\d"),
        )
    }

    @Test
    fun inlineCode() {
        assertEquals(
            document(
                paragraph(
                    Code("a`b"),
                    Text(" "),
                    Code(" padded "),
                    Text(" "),
                    Code("\\*raw&amp;"),
                    Text(" "),
                    Code("multi line"),
                ),
            ),
            parseMarkdown("`` a`b `` `  padded  ` `\\*raw&amp;` `multi\n  line`"),
        )
    }

    @Test
    fun inlineCodeOfOnlySpacesIsNotStripped() {
        assertEquals(
            document(paragraph(Text("x "), Code("  "))),
            parseMarkdown("x `  `"),
        )
    }

    @Test
    fun inlineLinks() {
        assertEquals(
            document(
                paragraph(
                    Link(
                        destination = "https://example.com/a_b?q=1&r=2",
                        title = "Title",
                        inlines = listOf(Text("plain "), Code("code"), Text(" "), Strong(listOf(Text("bold")))),
                    ),
                    Text(" "),
                    Link(destination = "https://example.com/a b", title = "T", inlines = listOf(Text("angle"))),
                ),
            ),
            parseMarkdown(
                "[plain `code` **bold**](https://example.com/a_b?q=1&amp;r=2 \"Title\") [angle](<https://example.com/a b> 'T')",
            ),
        )
    }

    @Test
    fun linkWithEmptyDestinationIsText() {
        assertEquals(
            document(paragraph(Text("empty"))),
            parseMarkdown("[empty]()"),
        )
    }

    @Test
    fun autolinks() {
        assertEquals(
            document(
                paragraph(
                    Link(
                        destination = "https://kotlinlang.org",
                        title = null,
                        inlines = listOf(Text("https://kotlinlang.org")),
                    ),
                    Text(" "),
                    Link(destination = "mailto:me@example.com", title = null, inlines = listOf(Text("me@example.com"))),
                    Text(" https://not.autolinked.in.commonmark"),
                ),
            ),
            parseMarkdown("<https://kotlinlang.org> <me@example.com> https://not.autolinked.in.commonmark"),
        )
    }

    @Test
    fun referenceLinks() {
        assertEquals(
            document(
                paragraph(
                    Link(destination = "https://ref.example", title = "Ref", inlines = listOf(Text("ref"))),
                    Text(" "),
                    Link(destination = "https://ref.example", title = "Ref", inlines = listOf(Text("text"))),
                    Text(" [unknown]"),
                ),
            ),
            parseMarkdown("[ref] [text][REF] [unknown]\n\n[ref]: https://ref.example \"Ref\""),
        )
    }

    @Test
    fun atxHeadings() {
        assertEquals(
            document(
                Heading(1, listOf(Text("One"))),
                Heading(2, listOf(Text("Two "), Code("code"))),
                Heading(3, listOf(Text("Three"))),
                Heading(4, listOf(Text("Four"))),
                Heading(5, listOf(Text("Five"))),
                Heading(6, listOf(Text("Six"))),
            ),
            parseMarkdown("# One\n## Two `code` ##\n### Three\n#### Four\n##### Five\n###### Six\n#\n"),
        )
    }

    @Test
    fun setextHeadings() {
        assertEquals(
            document(
                Heading(1, listOf(Text("One"))),
                Heading(2, listOf(Text("Two"))),
            ),
            parseMarkdown("One\n===\n\nTwo\n---"),
        )
    }

    @Test
    fun tightBulletList() {
        assertEquals(
            document(
                BulletList(
                    items = listOf(item(paragraph(Text("a"))), item(paragraph(Text("b"))), item(paragraph(Text("c")))),
                    isTight = true,
                ),
            ),
            parseMarkdown("- a\n- b\n- c"),
        )
    }

    @Test
    fun looseBulletList() {
        assertEquals(
            document(
                BulletList(
                    items = listOf(item(paragraph(Text("a"))), item(paragraph(Text("b")))),
                    isTight = false,
                ),
            ),
            parseMarkdown("- a\n\n- b"),
        )
    }

    @Test
    fun nestedLists() {
        assertEquals(
            document(
                BulletList(
                    items = listOf(
                        item(
                            paragraph(Text("a")),
                            OrderedList(
                                startNumber = 1,
                                items = listOf(
                                    item(
                                        paragraph(Text("a1")),
                                        BulletList(items = listOf(item(paragraph(Text("deep")))), isTight = true),
                                    ),
                                    item(paragraph(Text("a2"))),
                                ),
                                isTight = true,
                            ),
                        ),
                        item(paragraph(Text("b"))),
                    ),
                    isTight = true,
                ),
            ),
            parseMarkdown("- a\n  1. a1\n     - deep\n  2. a2\n- b"),
        )
    }

    @Test
    fun orderedListStartNumber() {
        assertEquals(
            document(
                OrderedList(
                    startNumber = 3,
                    items = listOf(item(paragraph(Text("three"))), item(paragraph(Text("four")))),
                    isTight = true,
                ),
            ),
            parseMarkdown("3) three\n4) four"),
        )
    }

    @Test
    fun listItemWithMultipleParagraphsAndContinuationLines() {
        assertEquals(
            document(
                OrderedList(
                    startNumber = 1,
                    items = listOf(
                        item(
                            paragraph(Text("first"), SoftBreak, Text("continued")),
                            paragraph(Text("second")),
                        ),
                    ),
                    isTight = false,
                ),
            ),
            parseMarkdown("1. first\n   continued\n\n   second"),
        )
    }

    @Test
    fun emptyListItem() {
        assertEquals(
            document(BulletList(items = listOf(item(paragraph(Text("a"))), item()), isTight = true)),
            parseMarkdown("- a\n-"),
        )
    }

    @Test
    fun fencedCodeBlock() {
        assertEquals(
            document(
                CodeBlock(
                    code = "fun main() {\n    println(\"&amp; \\*\")\n\n}",
                    language = "kotlin",
                ),
            ),
            parseMarkdown("```kotlin title=\"Main.kt\"\nfun main() {\n    println(\"&amp; \\*\")\n\n}\n```"),
        )
    }

    @Test
    fun fencedCodeBlockVariants() {
        assertEquals(
            document(
                CodeBlock(code = "no language", language = null),
                CodeBlock(code = "tildes", language = "sh"),
                CodeBlock(code = "", language = "kotlin"),
                CodeBlock(code = "a\n  b\nc", language = null),
            ),
            parseMarkdown(
                "```\nno language\n```\n\n~~~sh\ntildes\n~~~\n\n```kotlin\n```\n\n  ```\n  a\n    b\n c\n  ```",
            ),
        )
    }

    @Test
    fun unclosedFencedCodeBlock() {
        assertEquals(
            document(
                paragraph(Text("Intro")),
                CodeBlock(code = "val a = 1\nval b = 2", language = "kotlin"),
            ),
            parseMarkdown("Intro\n\n```kotlin\nval a = 1\nval b = 2\n"),
        )
    }

    @Test
    fun fencedCodeBlockInsideNestedListItem() {
        assertEquals(
            document(
                BulletList(
                    items = listOf(
                        item(
                            paragraph(Text("outer")),
                            BulletList(
                                items = listOf(
                                    item(
                                        paragraph(Text("inner")),
                                        CodeBlock(code = "fun x() {\n    y()\n}", language = "kotlin"),
                                    ),
                                ),
                                isTight = false,
                            ),
                        ),
                    ),
                    isTight = true,
                ),
            ),
            parseMarkdown("- outer\n  - inner\n\n    ```kotlin\n    fun x() {\n        y()\n    }\n    ```"),
        )
    }

    @Test
    fun indentedCodeBlock() {
        assertEquals(
            document(
                paragraph(Text("Intro")),
                CodeBlock(code = "val a = 1\n  val b = 2", language = null),
            ),
            parseMarkdown("Intro\n\n    val a = 1\n      val b = 2\n"),
        )
    }

    @Test
    fun blockQuote() {
        assertEquals(
            document(
                BlockQuote(
                    listOf(
                        paragraph(
                            Text("quoted "),
                            Emphasis(listOf(Text("text"))),
                            SoftBreak,
                            Text("lazy continuation"),
                        ),
                        BulletList(items = listOf(item(paragraph(Text("item")))), isTight = true),
                        CodeBlock(code = "code", language = "kotlin"),
                    ),
                ),
            ),
            parseMarkdown("> quoted *text*\nlazy continuation\n>\n> - item\n>\n> ```kotlin\n> code\n> ```"),
        )
    }

    @Test
    fun thematicBreaks() {
        assertEquals(
            document(paragraph(Text("a")), ThematicBreak, paragraph(Text("b")), ThematicBreak),
            parseMarkdown("a\n\n---\n\nb\n\n***"),
        )
    }

    @Test
    fun imagesAreReplacedByAltText() {
        assertEquals(
            document(paragraph(Text("Logo: Kotlin logo here"))),
            parseMarkdown("Logo: ![Kotlin *logo*](https://example.com/logo.png) here"),
        )
    }

    @Test
    fun rawHtmlIsLiteralText() {
        assertEquals(
            document(
                paragraph(Text("inline <span>html</span>")),
                paragraph(Text("<div>"), HardBreak, Text("block"), HardBreak, Text("</div>")),
            ),
            parseMarkdown("inline <span>html</span>\n\n<div>\nblock\n</div>"),
        )
    }

    @Test
    fun unsupportedGfmSyntaxIsLiteralText() {
        assertEquals(
            document(
                paragraph(Text("~~strike~~")),
                paragraph(Text("| a | b |"), SoftBreak, Text("| - | - |")),
            ),
            parseMarkdown("~~strike~~\n\n| a | b |\n| - | - |"),
        )
    }

    private fun document(vararg blocks: MarkdownBlock) = MarkdownDocument(blocks.toList())

    private fun paragraph(vararg inlines: MarkdownInline) = Paragraph(inlines.toList())

    private fun item(vararg blocks: MarkdownBlock) = MarkdownListItem(blocks.toList())
}
