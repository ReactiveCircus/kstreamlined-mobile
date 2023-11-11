package io.github.reactivecircus.kstreamlined.kmp.data.networking

import com.apollographql.apollo3.cache.normalized.ApolloStore
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object ApolloClientConfigs {
    val apolloStore = ApolloStore(
        normalizedCacheFactory = MemoryCacheFactory(
            maxSizeBytes = MaxSizeBytes,
            expireAfterMillis = CacheExpiry.toLong(DurationUnit.MILLISECONDS)
        )
    )
}

// 10 MB
private const val MaxSizeBytes = 10 * 1024 * 1024

// 5 minutes
private val CacheExpiry = 5.toDuration(DurationUnit.MINUTES)
