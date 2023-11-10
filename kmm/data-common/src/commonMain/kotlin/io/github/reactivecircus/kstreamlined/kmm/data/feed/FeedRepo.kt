package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedSource

interface FeedRepo {

    suspend fun loadFeedSources(
        refresh: Boolean = false
    ): List<FeedSource>

    suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>
}
