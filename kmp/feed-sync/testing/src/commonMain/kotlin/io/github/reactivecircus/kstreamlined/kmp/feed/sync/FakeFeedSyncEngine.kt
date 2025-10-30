package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.Turbine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeFeedSyncEngine : FeedSyncEngine {
    public val recordedSyncs: Turbine<RecordedSync> = Turbine()

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)

    override val syncState: StateFlow<SyncState> = _syncState

    public fun emitSyncState(newState: SyncState) {
        _syncState.value = newState
    }

    override suspend fun sync(forceRefresh: Boolean) {
        recordedSyncs.add(RecordedSync(forceRefresh))
    }

    public data class RecordedSync(
        val forceRefresh: Boolean,
    )
}
