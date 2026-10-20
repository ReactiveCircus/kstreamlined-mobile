package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.MarkdownInline

internal fun List<MarkdownInline>.toAnnotatedString(style: MarkdownStyle): AnnotatedString = buildAnnotatedString {
    appendInlines(this@toAnnotatedString, style)
}

private fun AnnotatedString.Builder.appendInlines(inlines: List<MarkdownInline>, style: MarkdownStyle) {
    inlines.forEach { inline ->
        when (inline) {
            is MarkdownInline.Text -> append(inline.text)

            is MarkdownInline.Code -> appendInlineCode(inline.code, style.inlineCode)

            is MarkdownInline.Emphasis -> withStyle(style.emphasis) { appendInlines(inline.inlines, style) }

            is MarkdownInline.Strong -> withStyle(style.strong) { appendInlines(inline.inlines, style) }

            is MarkdownInline.Link -> withLink(LinkAnnotation.Url(url = inline.destination, styles = style.link)) {
                appendInlines(inline.inlines, style)
            }

            MarkdownInline.SoftBreak -> append(' ')

            MarkdownInline.HardBreak -> append('\n')
        }
    }
}
