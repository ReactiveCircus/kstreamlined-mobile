package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedSource

public interface FeedDataSource {

    public suspend fun loadFeedOrigins(
        refresh: Boolean = false
    ): List<FeedSource>

    public suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>
}
