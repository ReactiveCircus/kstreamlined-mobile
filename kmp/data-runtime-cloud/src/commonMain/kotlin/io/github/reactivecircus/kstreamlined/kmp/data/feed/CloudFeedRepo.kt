package io.github.reactivecircus.kstreamlined.kmp.data.feed

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper.toApollo
import io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper.toModel
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.data.networking.defaultFetchPolicy

class CloudFeedRepo(private val apolloClient: ApolloClient) : FeedRepo {

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSource> {
        return runCatching {
            apolloClient.query(FeedSourcesQuery())
                .defaultFetchPolicy(refresh)
                .execute()
                .dataOrThrow().feedSources
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().map { it.toModel() }
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSource.Key>?,
        refresh: Boolean,
    ): List<FeedEntry> {
        return runCatching {
            apolloClient.query(
                FeedEntriesQuery(
                    filters = Optional.presentIfNotNull(filters?.map { it.toApollo() })
                )
            )
                .defaultFetchPolicy(refresh)
                .execute()
                .dataOrThrow().feedEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().map { it.toModel() }
    }
}
