package io.github.reactivecircus.kstreamlined.kmp.feed.model

import androidx.compose.runtime.Immutable

@Immutable
public data class DisplayableFeedItem<T : FeedItem>(
    val value: T,
    val displayablePublishTime: String,
)

public fun <T : FeedItem> T.toDisplayable(
    displayablePublishTime: String,
): DisplayableFeedItem<T> {
    return DisplayableFeedItem(
        value = this,
        displayablePublishTime = displayablePublishTime,
    )
}
