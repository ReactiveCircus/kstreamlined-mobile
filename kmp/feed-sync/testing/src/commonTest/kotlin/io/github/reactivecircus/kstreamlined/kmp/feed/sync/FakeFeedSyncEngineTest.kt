package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeFeedSyncEngineTest {

    private val fakeFeedSyncEngine = FakeFeedSyncEngine()

    @Test
    fun `syncState emits when new value is set by emitSyncState`() = runTest {
        fakeFeedSyncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())

            fakeFeedSyncEngine.emitSyncState(SyncState.Syncing)

            assertEquals(SyncState.Syncing, awaitItem())

            fakeFeedSyncEngine.emitSyncState(SyncState.Idle)

            assertEquals(SyncState.Idle, awaitItem())

            fakeFeedSyncEngine.emitSyncState(SyncState.Syncing)

            assertEquals(SyncState.Syncing, awaitItem())

            fakeFeedSyncEngine.emitSyncState(SyncState.OutOfSync)

            assertEquals(SyncState.OutOfSync, awaitItem())
        }
    }

    @Test
    fun `sync requests are recorded`() = runTest {
        fakeFeedSyncEngine.sync(forceRefresh = false)
        fakeFeedSyncEngine.sync(forceRefresh = true)
        fakeFeedSyncEngine.sync(forceRefresh = false)
        fakeFeedSyncEngine.sync(forceRefresh = false)

        assertEquals(
            FakeFeedSyncEngine.RecordedSync(forceRefresh = false),
            fakeFeedSyncEngine.recordedSyncs.awaitItem(),
        )
        assertEquals(
            FakeFeedSyncEngine.RecordedSync(forceRefresh = true),
            fakeFeedSyncEngine.recordedSyncs.awaitItem(),
        )
        assertEquals(
            FakeFeedSyncEngine.RecordedSync(forceRefresh = false),
            fakeFeedSyncEngine.recordedSyncs.awaitItem(),
        )
        assertEquals(
            FakeFeedSyncEngine.RecordedSync(forceRefresh = false),
            fakeFeedSyncEngine.recordedSyncs.awaitItem(),
        )
    }
}
