package io.github.reactivecircus.kstreamlined.kmp.data.feed

import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedOriginItem

public data class FeedSyncState(
    val feedOrigins: List<FeedOriginItem>,
    val feedItems: List<FeedItem>,
)
