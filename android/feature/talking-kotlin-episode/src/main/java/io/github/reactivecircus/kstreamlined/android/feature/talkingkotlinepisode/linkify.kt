package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

/**
 * Adopted from https://stackoverflow.com/a/73662287
 */
internal fun String.linkify(
    linkStyle: SpanStyle
): AnnotatedString = buildAnnotatedString {
    append(this@linkify)
    val spannable = SpannableString(this@linkify)
    Linkify.addLinks(spannable, Linkify.WEB_URLS)
    val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
    for (span in spans) {
        val start = spannable.getSpanStart(span)
        val end = spannable.getSpanEnd(span)
        addStyle(
            start = start,
            end = end,
            style = linkStyle,
        )
        addStringAnnotation(
            tag = UrlTag,
            annotation = span.url,
            start = start,
            end = end,
        )
    }
}

internal fun AnnotatedString.findUrl(offset: Int): String? =
    getStringAnnotations(UrlTag, offset, offset)
        .firstOrNull()
        ?.item

private const val UrlTag = "URL"
