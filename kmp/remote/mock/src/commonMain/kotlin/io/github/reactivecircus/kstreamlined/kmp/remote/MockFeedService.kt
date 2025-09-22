package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

public class MockFeedService : FeedService {
    override suspend fun fetchFeedOrigins(): List<FeedSource> = MockFeedSources

    override suspend fun fetchFeedEntries(filters: List<FeedSource.Key>?): List<FeedEntry> {
        return MockFeedEntries
    }

    override suspend fun fetchFeedEntriesAndOrigins(
        filters: List<FeedSource.Key>?,
    ): Pair<List<FeedEntry>, List<FeedSource>> = MockFeedEntries to MockFeedSources

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        return MockKotlinWeeklyIssueEntries
    }
}
