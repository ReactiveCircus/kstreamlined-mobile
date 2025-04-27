package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HtmlContentParserTest {

    @Test
    fun `tryParseHtml returns null for non-HTML content`() {
        assertNull(tryParseHtml("This is plain text"))
        assertNull(tryParseHtml(""))
        assertNull(tryParseHtml("   \n  \t  "))
    }

    @Test
    fun `tryParseHtml returns Document for HTML content`() {
        assertNotNull(tryParseHtml("<p>This is <b>HTML</b> content</p>"))
        assertNotNull(
            tryParseHtml(
                """
                    <p>This is a paragraph with a <a href="https://example.com">link</a> and a list:</p>
                    <ul>
                        <li>First item</li>
                        <li>Second item with <b>bold</b> text</li>
                    </ul>
                """.trimIndent()
            )
        )
        assertNotNull(tryParseHtml("<p>This is &quot;quoted&quot; text with &amp; ampersand</p>"))
        assertNotNull(tryParseHtml("Plain text followed by <p>HTML</p>"))
        assertNotNull(
            tryParseHtml(
                """
                    Some plain text at the start
                    <p>Followed by HTML content</p>
                    <ul>
                        <li>With a list</li>
                    </ul>
                """.trimIndent()
            )
        )
    }
}
