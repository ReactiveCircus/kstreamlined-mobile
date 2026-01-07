package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.Turbine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeFeedSyncEngine : FeedSyncEngine {
    public val recordedSyncs: Turbine<RecordedSync> = Turbine()

    // TODO remove `final` once migrated to 2.3.20
    final override val syncState: StateFlow<SyncState>
        field = MutableStateFlow<SyncState>(SyncState.Idle)

    public fun emitSyncState(newState: SyncState) {
        syncState.value = newState
    }

    override suspend fun sync(forceRefresh: Boolean) {
        recordedSyncs.add(RecordedSync(forceRefresh))
    }

    public data class RecordedSync(
        val forceRefresh: Boolean,
    )
}
