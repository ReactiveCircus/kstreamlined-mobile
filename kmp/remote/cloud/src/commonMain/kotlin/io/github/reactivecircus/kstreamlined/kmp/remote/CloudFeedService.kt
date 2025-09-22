package io.github.reactivecircus.kstreamlined.kmp.remote

import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesWithSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asApolloModel
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

public class CloudFeedService(private val apolloClient: ApolloClient) : FeedService {
    override suspend fun fetchFeedOrigins(): List<FeedSource> {
        return runCatching {
            apolloClient.query(FeedSourcesQuery())
                .fetchPolicy(FetchPolicy.NetworkOnly)
                .execute()
                .dataOrThrow()
                .feedSources
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().mapNotNull { it.feedSourceItem.asExternalModel() }
    }

    override suspend fun fetchFeedEntries(filters: List<FeedSource.Key>?): List<FeedEntry> {
        return runCatching {
            apolloClient.query(
                FeedEntriesQuery(
                    filters = Optional.presentIfNotNull(filters?.map { it.asApolloModel() }),
                ),
            )
                .fetchPolicy(FetchPolicy.NetworkOnly)
                .execute()
                .dataOrThrow()
                .feedEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().map { it.feedEntryItem.asExternalModel() }
    }

    override suspend fun fetchFeedEntriesAndOrigins(
        filters: List<FeedSource.Key>?,
    ): Pair<List<FeedEntry>, List<FeedSource>> {
        return runCatching {
            apolloClient.query(
                FeedEntriesWithSourcesQuery(
                    filters = Optional.presentIfNotNull(filters?.map { it.asApolloModel() }),
                ),
            )
                .fetchPolicy(FetchPolicy.NetworkOnly)
                .execute()
                .dataOrThrow()
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().let { result ->
            result.feedEntries.map {
                it.feedEntryItem.asExternalModel()
            } to result.feedSources.mapNotNull {
                it.feedSourceItem.asExternalModel()
            }
        }
    }

    override suspend fun fetchKotlinWeeklyIssue(
        url: String,
    ): List<KotlinWeeklyIssueEntry> {
        return runCatching {
            apolloClient.query(
                KotlinWeeklyIssueQuery(url = url),
            )
                .fetchPolicy(FetchPolicy.CacheFirst)
                .execute()
                .dataOrThrow()
                .kotlinWeeklyIssueEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().mapNotNull { it.asExternalModel() }
    }
}
