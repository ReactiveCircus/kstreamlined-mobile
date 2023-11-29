package io.github.reactivecircus.kstreamlined.android.common.ui.feed

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

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
