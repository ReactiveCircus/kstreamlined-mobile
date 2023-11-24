package io.github.reactivecircus.kstreamlined.kmp.data.feed

import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedOrigin

public data class FeedSyncState(
    val feedOrigins: List<FeedOrigin>,
    val feedItems: List<FeedItem>,
)
