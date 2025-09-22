package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import kotlinx.coroutines.flow.StateFlow

public interface FeedSyncEngine {
    public val syncState: StateFlow<SyncState>

    public suspend fun sync(forceRefresh: Boolean = false)
}

public sealed interface SyncState {
    public data object Syncing : SyncState

    public data object Idle : SyncState

    public data object OutOfSync : SyncState
}
