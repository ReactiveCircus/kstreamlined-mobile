package io.github.reactivecircus.kstreamlined.kmp.datasource

import io.github.reactivecircus.kstreamlined.kmp.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.datasource.model.FeedSource

class EdgeFeedDataSource : FeedDataSource {

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
