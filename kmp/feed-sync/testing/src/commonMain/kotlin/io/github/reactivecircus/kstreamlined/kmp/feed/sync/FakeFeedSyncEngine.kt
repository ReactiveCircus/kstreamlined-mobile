package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.Turbine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class FakeFeedSyncEngine : FeedSyncEngine {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Initializing)

    public val recordedSyncs: Turbine<RecordedSync> = Turbine()

    public fun emitSyncState(syncState: SyncState) {
        _syncState.value = syncState
    }

    override val syncState: StateFlow<SyncState> = _syncState

    override suspend fun sync(forceRefresh: Boolean) {
        recordedSyncs.add(RecordedSync(forceRefresh))
    }

    public data class RecordedSync(
        val forceRefresh: Boolean
    )
}
