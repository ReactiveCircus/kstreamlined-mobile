package io.github.reactivecircus.kstreamlined.kmp.data.feed

import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedSource

class FakeFeedRepo : FeedRepo {

    var nextFeedSourcesResponse: suspend () -> List<FeedSource> = {
        FakeFeedSources
    }

    var nextFeedEntriesResponse: suspend (
        filters: List<FeedSource.Key>?
    ) -> List<FeedEntry> = {
        FakeFeedEntries
    }

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }
}
