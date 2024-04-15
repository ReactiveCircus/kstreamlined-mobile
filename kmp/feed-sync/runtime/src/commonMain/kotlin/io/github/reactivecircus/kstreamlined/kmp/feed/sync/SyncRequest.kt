package io.github.reactivecircus.kstreamlined.kmp.feed.sync

internal class SyncRequest(
    val forceRefresh: Boolean,
    val skipFeedSources: Boolean = false,
)
