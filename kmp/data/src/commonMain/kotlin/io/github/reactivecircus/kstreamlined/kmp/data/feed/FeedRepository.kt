package io.github.reactivecircus.kstreamlined.kmp.data.feed

import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FeedRepository(
    private val feedDataSource: FeedDataSource
) {

    public val feedSyncState: Flow<FeedSyncState> = emptyFlow()

    public suspend fun syncNow() {
        feedDataSource.loadFeedEntries(null, true).also {
            Logger.i("<<Number of entries: ${it.size}>>")
            it.forEach { entry ->
                Logger.i("${entry::class.simpleName}: ${entry.title}, ${entry.publishTime}}")
            }
        }
    }

    public suspend fun selectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun unselectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun loadSavedFeedItems(): List<FeedItem> {
        TODO()
    }
}
