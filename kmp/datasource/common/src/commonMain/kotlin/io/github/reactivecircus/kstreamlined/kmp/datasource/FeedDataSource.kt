package io.github.reactivecircus.kstreamlined.kmp.datasource

import io.github.reactivecircus.kstreamlined.kmp.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.datasource.model.FeedSource

interface FeedDataSource {

    suspend fun loadFeedSources(
        refresh: Boolean = false
    ): List<FeedSource>

    suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>
}
