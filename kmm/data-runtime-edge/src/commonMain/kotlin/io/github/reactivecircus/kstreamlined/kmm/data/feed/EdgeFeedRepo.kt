package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedSource

class EdgeFeedRepo : FeedRepo {

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSource> {
        TODO()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        TODO()
    }
}
