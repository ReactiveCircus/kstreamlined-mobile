package io.github.reactivecircus.kstreamlined.kmp.networking

import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public interface FeedService {

    public suspend fun fetchFeedOrigins(
        refresh: Boolean = false
    ): List<FeedSource>

    public suspend fun fetchFeedEntries(
        filters: List<FeedSource.Key>? = null,
        refresh: Boolean = false,
    ): List<FeedEntry>

    public suspend fun fetchKotlinWeeklyIssue(
        url: String
    ): List<KotlinWeeklyIssueEntry>
}
