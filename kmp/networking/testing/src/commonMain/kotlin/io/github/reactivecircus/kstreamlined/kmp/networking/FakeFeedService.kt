package io.github.reactivecircus.kstreamlined.kmp.networking

import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public class FakeFeedService : FeedService {

    public var nextFeedSourcesResponse: suspend () -> List<FeedSource> = {
        FakeFeedSources
    }

    public var nextFeedEntriesResponse: suspend (
        filters: List<FeedSource.Key>?
    ) -> List<FeedEntry> = {
        FakeFeedEntries
    }

    public var nextKotlinWeeklyIssueResponse: suspend (
        url: String
    ) -> List<KotlinWeeklyIssueEntry> = {
        FakeKotlinWeeklyIssueEntries
    }

    override suspend fun fetchFeedOrigins(refresh: Boolean): List<FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun fetchFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        return nextKotlinWeeklyIssueResponse(url)
    }
}
