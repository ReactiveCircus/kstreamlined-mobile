package io.github.reactivecircus.kstreamlined.kmp.networking

import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public class EdgeFeedService : FeedService {

    override suspend fun fetchFeedOrigins(): List<FeedSource> {
        TODO()
    }

    override suspend fun fetchFeedEntries(filters: List<FeedSource.Key>?): List<FeedEntry> {
        TODO()
    }

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        TODO()
    }
}
