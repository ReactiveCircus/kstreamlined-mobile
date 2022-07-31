package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.FeedSourceKey

interface FeedRepo {

    suspend fun loadFeedSources(
        refresh: Boolean = false
    ): List<FeedSourcesQuery.FeedSource>

    suspend fun loadFeedEntries(
        filters: List<FeedSourceKey>? = null,
        refresh: Boolean = false,
    ): List<FeedEntriesQuery.FeedEntry>
}
