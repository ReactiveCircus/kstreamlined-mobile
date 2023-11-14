package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

interface FeedDataSource {

    suspend fun loadFeedOrigins(
        refresh: Boolean = false
    ): List<FeedOrigin>

    suspend fun loadFeedEntries(
        filters: List<FeedOrigin.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>
}
