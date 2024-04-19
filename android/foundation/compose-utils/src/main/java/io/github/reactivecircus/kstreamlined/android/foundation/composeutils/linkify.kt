package io.github.reactivecircus.kstreamlined.android.foundation.composeutils

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
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
        addLink(
            url = LinkAnnotation.Url(
                url = span.url,
                style = linkStyle,
            ),
            start = start,
            end = end,
        )
    }
}
