package io.github.reactivecircus.kstreamlined.kmp.remote.apollo

import com.apollographql.cache.normalized.CacheManager
import com.apollographql.cache.normalized.api.FieldPolicyCacheResolver
import com.apollographql.cache.normalized.api.TypePolicyCacheKeyGenerator
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import io.github.reactivecircus.kstreamlined.graphql.cache.Cache
import kotlin.time.DurationUnit
import kotlin.time.toDuration

public object ApolloClientConfigs {
    public val cacheManager: CacheManager = CacheManager(
        normalizedCacheFactory = MemoryCacheFactory(
            maxSizeBytes = MaxSizeBytes,
            expireAfterMillis = CacheExpiry.toLong(DurationUnit.MILLISECONDS),
        ),
        cacheKeyGenerator = TypePolicyCacheKeyGenerator(
            typePolicies = Cache.typePolicies,
        ),
        cacheResolver = FieldPolicyCacheResolver(
            fieldPolicies = Cache.fieldPolicies,
        ),
    )
}

// 10 MB
private const val MaxSizeBytes = 10 * 1024 * 1024

// 5 minutes
private val CacheExpiry = 5.toDuration(DurationUnit.MINUTES)
