package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document

internal fun tryParseHtml(text: String): Document? = Ksoup.parse(text).let { doc ->
    if (doc.body().childrenSize() == 0) null else doc
}
