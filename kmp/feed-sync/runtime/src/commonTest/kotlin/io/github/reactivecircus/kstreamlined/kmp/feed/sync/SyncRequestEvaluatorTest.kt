package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import io.github.reactivecircus.kstreamlined.kmp.database.SyncResourceType
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedSources
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class SyncRequestEvaluatorTest {

    private val db = createInMemoryDatabase()

    private val syncConfig = SyncConfig.Default

    private val fakeClock = FakeClock()

    private val syncRequestEvaluator = SyncRequestEvaluator(
        syncConfig = syncConfig,
        feedOriginEntityQueries = db.feedOriginEntityQueries,
        lastSyncMetadataQueries = db.lastSyncMetadataQueries,
        clock = fakeClock,
    )

    @Test
    fun `should not sync feed sources when skipFeedSources is true`() {
        val syncRequest = SyncRequest(forceRefresh = true, skipFeedSources = true)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertFalse(result.shouldSyncFeedSources)
    }

    @Test
    fun `should sync feed sources and feed items when forceRefresh is true`() {
        val syncRequest = SyncRequest(forceRefresh = true)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertTrue(result.shouldSyncFeedSources)
        assertTrue(result.shouldSyncFeedItems)
    }

    @Test
    fun `should sync feed sources when no lastSyncMetadata exists`() {
        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertTrue(result.shouldSyncFeedSources)
    }

    @Test
    fun `should not sync feed sources when cache has not expired`() {
        db.lastSyncMetadataQueries.updateLastSyncMetadata(
            resource_type = SyncResourceType.FeedOrigins,
            sync_params = "",
            last_sync_time = fakeClock.now(),
        )

        fakeClock.currentTime += syncConfig.feedSourcesCacheMaxAge

        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertFalse(result.shouldSyncFeedSources)
    }

    @Test
    fun `should sync feed sources when cache has expired`() {
        db.lastSyncMetadataQueries.updateLastSyncMetadata(
            resource_type = SyncResourceType.FeedOrigins,
            sync_params = "",
            last_sync_time = fakeClock.now(),
        )

        fakeClock.currentTime += syncConfig.feedSourcesCacheMaxAge + 1.seconds

        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertTrue(result.shouldSyncFeedSources)
    }

    @Test
    fun `should sync feed items when no lastSyncMetadata exists`() {
        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertTrue(result.shouldSyncFeedItems)
    }

    @Test
    fun `should not sync feed items when cache has not expired`() {
        db.transaction {
            FakeFeedSources.forEach {
                db.feedOriginEntityQueries.upsertFeedOrigin(
                    key = it.key.name,
                    title = it.title,
                    description = it.description,
                    selected = true,
                )
            }

            db.lastSyncMetadataQueries.updateLastSyncMetadata(
                resource_type = SyncResourceType.FeedItems,
                sync_params = FakeFeedSources
                    .sortedBy { it.key.name }
                    .joinToString(",") { it.key.name },
                last_sync_time = fakeClock.now(),
            )
        }

        fakeClock.currentTime += syncConfig.feedItemsCacheMaxAge

        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertFalse(result.shouldSyncFeedItems)
    }

    @Test
    fun `should sync feed items when cache has expired`() {
        db.transaction {
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = "key1",
                title = "title1",
                description = "desc1",
                selected = true,
            )
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = "key2",
                title = "title2",
                description = "desc2",
                selected = false,
            )
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = "key3",
                title = "title3",
                description = "desc3",
                selected = true,
            )

            db.lastSyncMetadataQueries.updateLastSyncMetadata(
                resource_type = SyncResourceType.FeedItems,
                sync_params = "key1,key3",
                last_sync_time = fakeClock.now(),
            )
        }

        fakeClock.currentTime += syncConfig.feedItemsCacheMaxAge + 1.seconds

        val syncRequest = SyncRequest(forceRefresh = false)
        val result = syncRequestEvaluator.evaluate(syncRequest)
        assertTrue(result.shouldSyncFeedItems)
    }
}
