package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.networking.model.FeedSource

internal fun List<FeedOriginEntity>.asNetworkModels(): List<FeedSource.Key>? {
    return filter { it.selected }
        .map { FeedSource.Key.valueOf(it.key) }
        .ifEmpty { null }
}

internal fun FeedSource.toDbModel(
    currentFeedOrigins: List<FeedOriginEntity>
): FeedOriginEntity {
    return FeedOriginEntity(
        key = key.name,
        title = title,
        description = description,
        selected = if (currentFeedOrigins.isNotEmpty()) {
            currentFeedOrigins.any { it.selected && it.key == key.name }
        } else {
            true
        },
    )
}

internal fun List<FeedOriginEntity>.toSyncParams(): String {
    return filter { it.selected }
        .sortedBy { it.key }
        .joinToString(",") { it.key }
}
