package io.github.reactivecircus.kstreamlined.kmp.data.feed

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin

public data class FeedSyncState(
    val feedOrigins: List<FeedOrigin>,
    val feedItems: List<FeedItem>,
)
