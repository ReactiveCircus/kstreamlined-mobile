package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource

internal fun List<FeedOriginEntity>.asSelectedFeedSourceKeys(): List<FeedSource.Key>? {
    return filter { it.selected }
        .map { FeedSource.Key.valueOf(it.key) }
        .ifEmpty { null }
}

internal fun FeedSource.toDbModel(
    currentFeedOrigins: List<FeedOriginEntity>,
): FeedOriginEntity {
    return FeedOriginEntity(
        key = key.name,
        title = title,
        description = description,
        selected = currentFeedOrigins.isEmpty() ||
            currentFeedOrigins.any { it.selected && it.key == key.name },
    )
}

internal fun List<FeedOriginEntity>.toSyncParams(): String {
    return filter { it.selected }
        .sortedBy { it.key }
        .joinToString(",") { it.key }
}
