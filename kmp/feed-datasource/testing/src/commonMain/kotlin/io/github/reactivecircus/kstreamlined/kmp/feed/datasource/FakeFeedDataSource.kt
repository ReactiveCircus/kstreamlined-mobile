package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.KotlinWeeklyIssueEntry

public class FakeFeedDataSource : FeedDataSource {

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

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedSource> {
        return nextFeedSourcesResponse()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return nextFeedEntriesResponse(filters)
    }

    override suspend fun loadKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        return nextKotlinWeeklyIssueResponse(url)
    }
}
