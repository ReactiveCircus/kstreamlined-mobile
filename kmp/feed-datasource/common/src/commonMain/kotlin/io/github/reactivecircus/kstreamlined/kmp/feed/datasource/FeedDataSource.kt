package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

public interface FeedDataSource {

    public suspend fun loadFeedOrigins(
        refresh: Boolean = false
    ): List<FeedOrigin>

    public suspend fun loadFeedEntries(
        filters: List<FeedOrigin.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>
}
