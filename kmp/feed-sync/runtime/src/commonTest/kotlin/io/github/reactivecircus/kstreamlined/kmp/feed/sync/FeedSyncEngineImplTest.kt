package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItemsLastSyncMetadata
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedOrigins
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedOriginsLastSyncMetadata
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toDbModel
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper.toSyncParams
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.FakeNetworkMonitor
import io.github.reactivecircus.kstreamlined.kmp.networkmonitor.NetworkState
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedEntries
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedSources
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class FeedSyncEngineImplTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val networkMonitor = FakeNetworkMonitor()

    private val fakeClock = FakeClock()

    private val syncConfig = SyncConfig.Default

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val syncEngine = FeedSyncEngineImpl(
        feedService = feedService,
        db = db,
        networkMonitor = networkMonitor,
        syncEngineScope = testScope.backgroundScope,
        syncEngineDispatcher = testDispatcher,
        clock = fakeClock,
    )

    @Test
    fun `initial automatic sync performed when DB is empty`() = testScope.runTest {
        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            assertFeedOriginsInDb(FakeFeedSources.map { it.toDbModel(emptyList()) })
            assertFeedItemsInDb(FakeFeedEntries.map { it.toDbModel(emptyList()) })
        }
    }

    @Test
    fun `initial automatic sync skipped when valid cache exists`() = testScope.runTest {
        val feedOrigins = FakeFeedSources.map {
            it.toDbModel(emptyList()).copy(selected = true)
        }
        with(db) {
            insertFeedOrigins(feedOrigins)
            insertFeedItems(FakeFeedEntries.map { it.toDbModel(emptyList()) })
            insertFeedOriginsLastSyncMetadata(
                lastSyncTime = fakeClock.now(),
            )
            insertFeedItemsLastSyncMetadata(
                syncParams = feedOrigins.toSyncParams(),
                lastSyncTime = fakeClock.now(),
            )
        }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
        }
    }

    @Test
    fun `automatic feed sources sync skipped when any feed source exists in DB regardless of expiry`() =
        testScope.runTest {
            val initialFeedOrigins = FakeFeedSources.map {
                it.toDbModel(emptyList()).copy(selected = true)
            }
            val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }
            with(db) {
                insertFeedOrigins(initialFeedOrigins)
                insertFeedItems(initialFeedItems)
                insertFeedOriginsLastSyncMetadata(
                    lastSyncTime = fakeClock.now(),
                )
                insertFeedOriginsLastSyncMetadata(
                    lastSyncTime = fakeClock.now(),
                )
            }

            feedService.nextFeedSourcesResponse = {
                FakeFeedSources.map { it.copy(title = "Updated ${it.title}") }
            }
            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries + newFeedEntry
            }

            // feed sources cache has expired
            fakeClock.currentTime += syncConfig.feedSourcesCacheMaxAge + 1.seconds

            syncEngine.syncState.test {
                assertEquals(SyncState.Idle, awaitItem())
                assertEquals(SyncState.Syncing, awaitItem())
                assertEquals(SyncState.Idle, awaitItem())

                advanceTimeBy(1.days)

                expectNoEvents()

                // feed sources should not have been updated
                assertFeedOriginsInDb(initialFeedOrigins)
                // feed items should have been updated with new item
                assertFeedItemsInDb(initialFeedItems + newFeedEntry.toDbModel(emptyList()))
            }
        }

    @Test
    fun `automatic sync triggered by feed sources changes in DB`() = testScope.runTest {
        val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            // feed items cache has expired
            advanceTimeBy(syncConfig.feedItemsCacheMaxAge + 1.seconds)
            fakeClock.currentTime += syncConfig.feedItemsCacheMaxAge + 1.seconds

            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries + newFeedEntry
            }

            // deselect a feed source
            db.feedOriginEntityQueries.updateSelection(
                selected = false,
                key = FeedSource.Key.KotlinBlog.name,
            )

            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            // feed items should have been updated with new item added
            // and item matching deselected feed source filtered out
            assertFeedItemsInDb(
                initialFeedItems - initialFeedItems.first {
                    it.feed_origin_key == FeedSource.Key.KotlinBlog.name
                } + newFeedEntry.toDbModel(emptyList())
            )
        }
    }

    @Test
    fun `automatic sync not triggered when network monitor emits Disconnected state`() = testScope.runTest {
        val initialFeedOrigins = FakeFeedSources.map {
            it.toDbModel(emptyList()).copy(selected = true)
        }
        val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            // feed items cache has expired
            advanceTimeBy(syncConfig.feedItemsCacheMaxAge + 1.seconds)
            fakeClock.currentTime += syncConfig.feedItemsCacheMaxAge + 1.seconds

            // NetworkMonitor emits NetworkState.Disconnected
            networkMonitor.emitNetworkState(NetworkState.Disconnected)

            advanceTimeBy(1.days)

            expectNoEvents()

            // feed sources should not have been updated
            assertFeedOriginsInDb(initialFeedOrigins)
            // feed items should not have been updated
            assertFeedItemsInDb(initialFeedItems)
        }
    }

    @Test
    fun `automatic sync not triggered when network monitor emits Connected state but SyncState is not OutOfSync`() = testScope.runTest {
        val initialFeedOrigins = FakeFeedSources.map {
            it.toDbModel(emptyList()).copy(selected = true)
        }
        val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            // feed items cache has expired
            advanceTimeBy(syncConfig.feedItemsCacheMaxAge + 1.seconds)
            fakeClock.currentTime += syncConfig.feedItemsCacheMaxAge + 1.seconds

            // NetworkMonitor emits NetworkState.Connected
            networkMonitor.emitNetworkState(NetworkState.Connected)

            advanceTimeBy(1.days)

            expectNoEvents()

            // feed sources should not have been updated
            assertFeedOriginsInDb(initialFeedOrigins)
            // feed items should not have been updated
            assertFeedItemsInDb(initialFeedItems)
        }
    }

    @Test
    fun `automatic sync triggered when network monitor emits Connected state and SyncState is OutOfSync`() = testScope.runTest {
        feedService.nextFeedSourcesResponse = {
            error("Failed to fetch feed sources")
        }
        feedService.nextFeedEntriesResponse = {
            error("Failed to fetch feed entries")
        }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.OutOfSync, awaitItem())

            feedService.nextFeedSourcesResponse = {
                FakeFeedSources
            }

            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries
            }

            // NetworkMonitor emits NetworkState.Connected
            networkMonitor.emitNetworkState(NetworkState.Connected)

            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.Idle, awaitItem())

            assertFeedOriginsInDb(FakeFeedSources.map { it.toDbModel(emptyList()) })
            assertFeedItemsInDb(FakeFeedEntries.map { it.toDbModel(emptyList()) })
        }
    }

    @Test
    fun `manual sync with forceRefresh = false performed when cache is expired`() =
        testScope.runTest {
            val initialFeedOrigins = FakeFeedSources.map {
                it.toDbModel(emptyList()).copy(selected = true)
            }
            val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }
            with(db) {
                insertFeedOrigins(initialFeedOrigins)
                insertFeedItems(initialFeedItems)
                insertFeedOriginsLastSyncMetadata(
                    lastSyncTime = fakeClock.now(),
                )
                insertFeedItemsLastSyncMetadata(
                    syncParams = initialFeedOrigins.toSyncParams(),
                    lastSyncTime = fakeClock.now(),
                )
            }

            val newFeedSources = FakeFeedSources.map { it.copy(title = "Updated ${it.title}") }
            feedService.nextFeedSourcesResponse = {
                newFeedSources
            }
            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries + newFeedEntry
            }

            syncEngine.syncState.test {
                assertEquals(SyncState.Idle, awaitItem())

                // caches have expired
                val advanceBy = maxOf(
                    syncConfig.feedSourcesCacheMaxAge,
                    syncConfig.feedItemsCacheMaxAge,
                ) + 1.seconds
                advanceTimeBy(advanceBy)
                fakeClock.currentTime += advanceBy

                syncEngine.sync(forceRefresh = false)

                assertEquals(SyncState.Syncing, awaitItem())
                assertEquals(SyncState.Idle, awaitItem())

                // feed sources should have been updated
                assertFeedOriginsInDb(newFeedSources.map { it.toDbModel(emptyList()) })
                // feed items should have been updated with new item
                assertFeedItemsInDb(initialFeedItems + newFeedEntry.toDbModel(emptyList()))
            }
        }

    @Test
    fun `manual sync with forceRefresh = false skipped when valid cache exists`() =
        testScope.runTest {
            val initialFeedOrigins = FakeFeedSources.map {
                it.toDbModel(emptyList()).copy(selected = true)
            }
            val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }
            with(db) {
                insertFeedOrigins(initialFeedOrigins)
                insertFeedItems(initialFeedItems)
                insertFeedOriginsLastSyncMetadata(
                    lastSyncTime = fakeClock.now(),
                )
                insertFeedItemsLastSyncMetadata(
                    syncParams = initialFeedOrigins.toSyncParams(),
                    lastSyncTime = fakeClock.now(),
                )
            }

            val newFeedSources = FakeFeedSources.map { it.copy(title = "Updated ${it.title}") }
            feedService.nextFeedSourcesResponse = {
                newFeedSources
            }
            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries + newFeedEntry
            }

            syncEngine.syncState.test {
                assertEquals(SyncState.Idle, awaitItem())

                // caches not expired
                val advanceBy = minOf(
                    syncConfig.feedSourcesCacheMaxAge,
                    syncConfig.feedItemsCacheMaxAge,
                ) - 1.seconds
                advanceTimeBy(advanceBy)
                fakeClock.currentTime += advanceBy

                syncEngine.sync(forceRefresh = false)

                advanceTimeBy(1.days)

                expectNoEvents()

                // feed sources should not have been updated
                assertFeedOriginsInDb(initialFeedOrigins)
                // feed items should not have been updated
                assertFeedItemsInDb(initialFeedItems)
            }
        }

    @Test
    fun `manual sync with forceRefresh = true performed when valid cache exists`() =
        testScope.runTest {
            val initialFeedOrigins = FakeFeedSources.map {
                it.toDbModel(emptyList()).copy(selected = true)
            }
            val initialFeedItems = FakeFeedEntries.map { it.toDbModel(emptyList()) }
            with(db) {
                insertFeedOrigins(initialFeedOrigins)
                insertFeedItems(initialFeedItems)
                insertFeedOriginsLastSyncMetadata(
                    lastSyncTime = fakeClock.now(),
                )
                insertFeedItemsLastSyncMetadata(
                    syncParams = initialFeedOrigins.toSyncParams(),
                    lastSyncTime = fakeClock.now(),
                )
            }

            val newFeedSources = FakeFeedSources.map { it.copy(title = "Updated ${it.title}") }
            feedService.nextFeedSourcesResponse = {
                newFeedSources
            }
            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries + newFeedEntry
            }

            syncEngine.syncState.test {
                assertEquals(SyncState.Idle, awaitItem())

                // caches not expired
                val advanceBy = minOf(
                    syncConfig.feedSourcesCacheMaxAge,
                    syncConfig.feedItemsCacheMaxAge,
                ) - 1.seconds
                advanceTimeBy(advanceBy)
                fakeClock.currentTime += advanceBy

                syncEngine.sync(forceRefresh = true)

                assertEquals(SyncState.Syncing, awaitItem())
                assertEquals(SyncState.Idle, awaitItem())

                // feed sources should have been updated
                assertFeedOriginsInDb(newFeedSources.map { it.toDbModel(emptyList()) })
                // feed items should have been updated with new item
                assertFeedItemsInDb(initialFeedItems + newFeedEntry.toDbModel(emptyList()))
            }
        }

    @Test
    fun `do not write to DB when either sync request fails`() = testScope.runTest {
        feedService.nextFeedSourcesResponse = {
            FakeFeedSources
        }
        feedService.nextFeedEntriesResponse = {
            error("Failed to fetch feed entries")
        }

        syncEngine.syncState.test {
            assertEquals(SyncState.Idle, awaitItem())
            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.OutOfSync, awaitItem())

            assertFeedOriginsInDb(emptyList())
            assertFeedItemsInDb(emptyList())

            feedService.nextFeedSourcesResponse = {
                error("Failed to fetch feed sources")
            }
            feedService.nextFeedEntriesResponse = {
                FakeFeedEntries
            }

            syncEngine.sync(forceRefresh = true)

            assertEquals(SyncState.Syncing, awaitItem())
            assertEquals(SyncState.OutOfSync, awaitItem())

            assertFeedOriginsInDb(emptyList())
            assertFeedItemsInDb(emptyList())
        }
    }

    private val newFeedEntry = FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-400",
        title = "Kotlin Weekly #400",
        publishTime = Instant.parse("2024-01-01T09:13:00Z"),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-400",
        issueNumber = 400,
    )

    private fun assertFeedOriginsInDb(expected: List<FeedOriginEntity>) {
        val savedFeedOrigins = db.feedOriginEntityQueries.allFeedOrigins().executeAsList()
        assertEquals(expected, savedFeedOrigins)
    }

    private fun assertFeedItemsInDb(expected: List<FeedItemEntity>) {
        val savedFeedItems = db.feedItemEntityQueries.feedItemsForSelectedOrigins().executeAsList()
        assertEquals(expected.sortedBy { it.id }, savedFeedItems.sortedBy { it.id })
    }
}
