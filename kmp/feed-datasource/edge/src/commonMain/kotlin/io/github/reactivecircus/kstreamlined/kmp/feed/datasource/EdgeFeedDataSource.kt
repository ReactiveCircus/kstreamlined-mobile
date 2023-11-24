package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedSource

public class EdgeFeedDataSource : FeedDataSource {

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedSource> {
        TODO()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        TODO()
    }
}
