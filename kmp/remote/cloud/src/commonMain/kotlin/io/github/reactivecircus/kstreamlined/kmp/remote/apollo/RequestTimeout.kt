package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.ExecutionContext
import com.apollographql.apollo.api.Operation
import kotlin.time.Duration

/**
 * Network timeout of an operation, overriding the default timeouts of the [TimeoutRoutingHttpEngine].
 */
internal data class RequestTimeout(val duration: Duration) : ExecutionContext.Element {
    init {
        require(duration.isPositive() && duration.isFinite()) {
            "Request timeout must be positive and finite but was $duration."
        }
    }

    override val key: ExecutionContext.Key<*> get() = Key

    companion object Key : ExecutionContext.Key<RequestTimeout>
}

/**
 * Executes this call with a network timeout of [duration].
 *
 * Only takes effect when the [com.apollographql.apollo.ApolloClient] is configured with
 * the [com.apollographql.apollo.network.http.HttpEngine] created by `createTimeoutRoutingHttpEngine`.
 */
internal fun <D : Operation.Data> ApolloCall<D>.requestTimeout(duration: Duration): ApolloCall<D> =
    addExecutionContext(RequestTimeout(duration))
