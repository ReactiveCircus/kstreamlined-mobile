package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.FeedSourceKey

class FakeFeedRepo : FeedRepo {

    var nextFeedSourcesResponse: suspend () -> List<FeedSourcesQuery.FeedSource> = {
        FakeFeedSources
    }

    var nextFeedEntriesResponse: suspend (
        filters: List<FeedSourceKey>?
    ) -> List<FeedEntriesQuery.FeedEntry> = {
        FakeFeedEntries
    }

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSourcesQuery.FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSourceKey>?,
        refresh: Boolean,
    ): List<FeedEntriesQuery.FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }
}
