package io.github.reactivecircus.kstreamlined.kmp.feed.sync

internal data class SyncRequest(
    val forceRefresh: Boolean,
    val skipFeedSources: Boolean = false,
)
