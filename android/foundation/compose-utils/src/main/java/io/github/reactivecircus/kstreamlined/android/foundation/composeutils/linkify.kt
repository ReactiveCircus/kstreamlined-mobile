package io.github.reactivecircus.kstreamlined.android.foundation.composeutils

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

/**
 * Linkify a [String] and return an [AnnotatedString].
 * Adopted from https://stackoverflow.com/a/73662287
 */
public fun String.linkify(
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

/**
 * Find the link at the given [offset] in the [AnnotatedString].
 */
public fun AnnotatedString.findUrl(offset: Int): String? =
    getStringAnnotations(UrlTag, offset, offset)
        .firstOrNull()
        ?.item

private const val UrlTag = "URL"
