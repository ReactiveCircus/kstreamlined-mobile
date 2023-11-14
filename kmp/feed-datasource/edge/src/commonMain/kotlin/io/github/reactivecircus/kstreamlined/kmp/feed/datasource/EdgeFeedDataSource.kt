package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

class EdgeFeedDataSource : FeedDataSource {

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedOrigin> {
        TODO()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedOrigin.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        TODO()
    }
}
