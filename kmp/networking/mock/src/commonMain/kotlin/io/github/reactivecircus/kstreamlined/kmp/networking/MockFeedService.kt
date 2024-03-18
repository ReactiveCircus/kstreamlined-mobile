package io.github.reactivecircus.kstreamlined.kmp.networking

import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public class MockFeedService : FeedService {

    override suspend fun fetchFeedOrigins(): List<FeedSource> {
        return MockFeedSources
    }

    override suspend fun fetchFeedEntries(filters: List<FeedSource.Key>?): List<FeedEntry> {
        return MockFeedEntries
    }

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        return MockKotlinWeeklyIssueEntries
    }
}
