package io.github.reactivecircus.kstreamlined.kmp.networking

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.kmp.networking.apollo.defaultFetchPolicy
import io.github.reactivecircus.kstreamlined.kmp.networking.mapper.asApolloModel
import io.github.reactivecircus.kstreamlined.kmp.networking.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.networking.model.KotlinWeeklyIssueEntry

public class CloudFeedService(private val apolloClient: ApolloClient) : FeedService {

    override suspend fun fetchFeedOrigins(refresh: Boolean): List<FeedSource> {
        return runCatching {
            apolloClient.query(FeedSourcesQuery())
                .defaultFetchPolicy(refresh)
                .execute()
                .dataOrThrow()
                .feedSources
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().mapNotNull { it.asExternalModel() }
    }

    override suspend fun fetchFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return runCatching {
            apolloClient.query(
                FeedEntriesQuery(
                    filters = Optional.presentIfNotNull(filters?.map { it.asApolloModel() })
                )
            )
                .defaultFetchPolicy(refresh)
                .execute()
                .dataOrThrow()
                .feedEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().map { it.asExternalModel() }
    }

    override suspend fun fetchKotlinWeeklyIssue(
        url: String
    ): List<KotlinWeeklyIssueEntry> {
        return runCatching {
            apolloClient.query(
                KotlinWeeklyIssueQuery(url = url)
            )
                .execute()
                .dataOrThrow()
                .kotlinWeeklyIssueEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().mapNotNull { it.asExternalModel() }
    }
}
