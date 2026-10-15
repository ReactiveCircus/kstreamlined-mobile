package io.github.reactivecircus.kstreamlined.kmp.remote

import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.github.reactivecircus.kstreamlined.graphql.KotlinWeeklyIssueQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

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
}
