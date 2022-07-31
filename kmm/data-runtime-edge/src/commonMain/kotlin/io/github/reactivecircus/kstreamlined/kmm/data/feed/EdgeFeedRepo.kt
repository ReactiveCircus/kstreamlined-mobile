package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.FeedSourceKey

class EdgeFeedRepo : FeedRepo {

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSourcesQuery.FeedSource> {
        TODO()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSourceKey>?,
        refresh: Boolean,
    ): List<FeedEntriesQuery.FeedEntry> {
        TODO()
    }
}
