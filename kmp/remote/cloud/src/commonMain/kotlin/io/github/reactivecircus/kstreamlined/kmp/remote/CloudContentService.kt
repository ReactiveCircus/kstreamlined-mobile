package io.github.reactivecircus.kstreamlined.kmp.remote

import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.github.reactivecircus.kstreamlined.graphql.KotlinBlogTldrQuery
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.apollo.requestTimeout
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.time.Duration.Companion.seconds

@ContributesBinding(AppScope::class)
internal class CloudContentService(private val apolloClient: ApolloClient) : ContentService {
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

    override suspend fun fetchKotlinBlogTldr(id: String): KotlinBlogTldr {
        return runCatching {
            apolloClient.query(
                KotlinBlogTldrQuery(id = id),
            )
                .requestTimeout(KotlinBlogTldrRequestTimeout)
                .fetchPolicy(FetchPolicy.CacheFirst)
                .execute()
                .dataOrThrow()
                .kotlinBlogTldr
        }.onFailure {
            Logger.w("Query failed", it)
        }.getOrThrow().asExternalModel()
    }
}

private val KotlinBlogTldrRequestTimeout = 30.seconds
