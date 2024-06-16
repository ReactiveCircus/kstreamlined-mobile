package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.Turbine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeFeedSyncEngine : FeedSyncEngine {

    public val recordedSyncs: Turbine<RecordedSync> = Turbine()

    public fun emitSyncState(newState: SyncState) {
        syncState.value = newState
    }

    override val syncState: StateFlow<SyncState>
        field = MutableStateFlow<SyncState>(SyncState.Initializing)

    override suspend fun sync(forceRefresh: Boolean) {
        recordedSyncs.add(RecordedSync(forceRefresh))
    }

    public data class RecordedSync(
        val forceRefresh: Boolean
    )
}
