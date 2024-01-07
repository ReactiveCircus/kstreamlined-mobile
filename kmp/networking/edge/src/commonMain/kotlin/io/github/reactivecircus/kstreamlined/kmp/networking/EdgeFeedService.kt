package io.github.reactivecircus.kstreamlined.kmp.networking

import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public class EdgeFeedService : FeedService {

    override suspend fun fetchFeedOrigins(refresh: Boolean): List<FeedSource> {
        TODO()
    }

    override suspend fun fetchFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        TODO()
    }

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        TODO()
    }
}
