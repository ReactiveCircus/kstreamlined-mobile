package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

class FakeFeedDataSource : FeedDataSource {

    var nextFeedSourcesResponse: suspend () -> List<FeedOrigin> = {
        FakeFeedOrigins
    }

    var nextFeedEntriesResponse: suspend (
        filters: List<FeedOrigin.Key>?
    ) -> List<FeedEntry> = {
        FakeFeedEntries
    }

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedOrigin> {
        return nextFeedSourcesResponse()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedOrigin.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }
}
