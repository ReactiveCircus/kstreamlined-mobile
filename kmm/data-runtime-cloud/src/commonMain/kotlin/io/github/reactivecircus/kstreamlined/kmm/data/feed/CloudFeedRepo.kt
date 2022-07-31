package io.github.reactivecircus.kstreamlined.kmm.data.feed

import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmm.data.networking.defaultFetchPolicy

class CloudFeedRepo(private val apolloClient: ApolloClient) : FeedRepo {

    override suspend fun loadFeedSources(refresh: Boolean): List<FeedSourcesQuery.FeedSource> {
        return runCatching {
            apolloClient.query(FeedSourcesQuery())
                .defaultFetchPolicy(refresh)
                .execute()
                .dataAssertNoErrors.feedSources
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow()
    }

    override suspend fun loadFeedEntries(
        filters: List<FeedSourceKey>?,
        refresh: Boolean,
    ): List<FeedEntriesQuery.FeedEntry> {
        return runCatching {
            apolloClient.query(FeedEntriesQuery(filters = Optional.presentIfNotNull(filters)))
                .defaultFetchPolicy(refresh)
                .execute()
                .dataAssertNoErrors.feedEntries
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow()
    }
}
