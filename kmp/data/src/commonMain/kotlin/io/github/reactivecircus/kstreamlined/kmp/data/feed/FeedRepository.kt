package io.github.reactivecircus.kstreamlined.kmp.data.feed

import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedOriginItem
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.flow.Flow

class FeedRepository(
    private val feedDataSource: FeedDataSource
) {

    val feedSyncState: Flow<FeedSyncState> = TODO()

    suspend fun syncNow() {
        feedDataSource.loadFeedEntries(null, true)
        TODO()
    }

    suspend fun selectFeedSource(feedOrigin: FeedOriginItem) {
        feedOrigin.selected
        TODO()
    }

    suspend fun unselectFeedSource(feedOrigin: FeedOriginItem) {
        feedOrigin.selected
        TODO()
    }

    suspend fun getSavedFeedItems() {
        TODO()
    }
}
