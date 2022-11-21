package io.github.reactivecircus.kstreamlined.kmm.data.networking

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy

/**
 * Sets the [FetchPolicy] for queries to [FetchPolicy.NetworkOnly] if [refresh] is true.
 */
internal fun <D : Operation.Data> ApolloCall<D>.defaultFetchPolicy(refresh: Boolean) = if (refresh) {
    fetchPolicy(FetchPolicy.NetworkOnly)
} else {
    this
}
