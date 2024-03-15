package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import kotlinx.coroutines.flow.StateFlow

public class FakeFeedSyncEngine : FeedSyncEngine {
    override val syncState: StateFlow<SyncState>
        get() = TODO("Not yet implemented")

    override suspend fun sync(forceRefresh: Boolean) {
        TODO("Not yet implemented")
    }
}
