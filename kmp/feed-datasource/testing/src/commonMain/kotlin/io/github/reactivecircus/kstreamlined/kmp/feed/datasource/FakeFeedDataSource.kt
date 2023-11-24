package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedSource

public class FakeFeedDataSource : FeedDataSource {

    public var nextFeedSourcesResponse: suspend () -> List<FeedSource> = {
        FakeFeedSources
    }

    public var nextFeedEntriesResponse: suspend (
        filters: List<FeedSource.Key>?
    ) -> List<FeedEntry> = {
        FakeFeedEntries
    }

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }
}
