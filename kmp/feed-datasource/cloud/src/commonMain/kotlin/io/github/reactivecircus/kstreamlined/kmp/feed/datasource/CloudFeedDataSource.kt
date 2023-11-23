package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asApolloModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.networking.defaultFetchPolicy

public class CloudFeedDataSource(private val apolloClient: ApolloClient) : FeedDataSource {

    override suspend fun loadFeedOrigins(refresh: Boolean): List<FeedOrigin> {
        return runCatching {
            apolloClient.query(FeedSourcesQuery())
                .defaultFetchPolicy(refresh)
                .execute()
                .dataOrThrow().feedSources
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().mapNotNull { it.asExternalModel() }
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedOrigin.Key>?,
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
                .dataOrThrow().feedEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().map { it.asExternalModel() }
    }
}
