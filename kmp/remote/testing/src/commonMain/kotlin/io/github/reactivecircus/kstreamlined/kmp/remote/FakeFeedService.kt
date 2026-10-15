package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource

public class FakeFeedService : FeedService {
    public var nextFeedSourcesResponse: suspend () -> List<FeedSource> = {
        FakeFeedSources
    }

    public var nextFeedEntriesResponse: suspend (
        filters: List<FeedSource.Key>?,
    ) -> List<FeedEntry> = {
        FakeFeedEntries
    }

    public var nextFeedEntriesAndOriginsResponse: suspend (
        filters: List<FeedSource.Key>?,
    ) -> Pair<List<FeedEntry>, List<FeedSource>> = {
        FakeFeedEntries to FakeFeedSources
    }

    override suspend fun fetchFeedOrigins(): List<FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun fetchFeedEntries(filters: List<FeedSource.Key>?): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }

    override suspend fun fetchFeedEntriesAndOrigins(
        filters: List<FeedSource.Key>?,
    ): Pair<List<FeedEntry>, List<FeedSource>> {
        return nextFeedEntriesAndOriginsResponse(filters)
    }
}
