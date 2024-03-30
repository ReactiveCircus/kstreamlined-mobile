package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedOrigins
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FakeFeedSyncEngine
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.SyncState
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalCoroutinesApi::class)
class HomePresenterTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val feedSyncEngine = FakeFeedSyncEngine()

    private val fixedClock = object : Clock {
        override fun now(): Instant {
            return "2023-12-03T03:10:54Z".toInstant()
        }
    }

    private val timeZone = TimeZone.UTC

    private val dummyFeedOrigins = listOf(
        FeedOriginEntity(
            key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = true,
        )
    )

    private val dummyFeedItems = listOf(
        FeedItemEntity(
            id = "1",
            feed_origin_key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog Post",
            publish_time = "2022-01-01T00:00:00Z".toInstant(),
            content_url = "content-url-1",
            image_url = "image-url-1",
            description = null,
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = false,
        )
    )

    private val homePresenter = HomePresenter(
        feedSyncEngine = feedSyncEngine,
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
    )

    @Test
    fun `uiState emits Loading state when initialized`() = testScope.runTest {
        homePresenter.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState emits Content state with refreshing = false and hasTransientError = false when feedOrigins and feedItems are not empty and syncState is Idle`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.Idle)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = false,
                )
            }
        }

    @Test
    fun `uiState emits Content state with refreshing = true and hasTransientError = false when feedOrigins and feedItems are not empty and syncState is Syncing`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.Idle)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = false,
                )

                feedSyncEngine.emitSyncState(SyncState.Syncing)

                assertContentState(
                    state = awaitItem(),
                    refreshing = true,
                    hasTransientError = false,
                )
            }
        }

    @Test
    fun `uiState emits Content state with refreshing = false and hasTransientError = true when feedOrigins and feedItems are not empty and syncState is OutOfSync`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                )
            }
        }

    @Test
    fun `uiState emits Content state with hasTransientError = true when current state is Content state with refreshing = true and OutOfSync syncState is emitted`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.Syncing)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = true,
                    hasTransientError = false,
                )

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                )
            }
        }

    @Test
    fun `uiState emits Error state when feedOrigins and feedItems are empty and syncState is OutOfSync`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                assertEquals(HomeUiState.Error, awaitItem())
            }
        }

    @Test
    fun `uiState emits Loading state when feedOrigins and feedItems are empty and syncState is Loading`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                assertEquals(HomeUiState.Error, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.Syncing)

                assertEquals(HomeUiState.Loading, awaitItem())
            }
        }

    @Test
    fun `uiState emits Loading state when feedOrigins and feedItems are empty and syncState is Idle`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                assertEquals(HomeUiState.Error, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.Idle)

                assertEquals(HomeUiState.Loading, awaitItem())
            }
        }

    @Test
    fun `sends sync request to sync engine when refresh is called`() = testScope.runTest {
        homePresenter.refresh()

        assertEquals(
            FakeFeedSyncEngine.RecordedSync(forceRefresh = true),
            feedSyncEngine.recordedSyncs.awaitItem(),
        )
    }

    @Test
    fun `uiState emits Content state with updated items when toggleSavedForLatter is called`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                )

                val feedItem = dummyFeedItems.first().let {
                    FeedItem.KotlinBlog(
                        id = it.id,
                        title = it.title,
                        publishTime = it.publish_time,
                        contentUrl = it.content_url,
                        savedForLater = it.saved_for_later,
                        featuredImageUrl = it.image_url.orEmpty(),
                    )
                }

                homePresenter.toggleSavedForLater(feedItem)

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                    items = listOf(feedItem.copy(savedForLater = true))
                        .toHomeFeedItems(fixedClock, timeZone),
                )

                homePresenter.toggleSavedForLater(feedItem.copy(savedForLater = true))

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                    items = listOf(feedItem.copy(savedForLater = false))
                        .toHomeFeedItems(fixedClock, timeZone),
                )
            }
        }

    @Test
    fun `uiState emits Content state with refreshing = false when dismissTransientError is called and current state is Content state`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                feedSyncEngine.emitSyncState(SyncState.OutOfSync)

                db.transaction {
                    db.insertFeedOrigins(dummyFeedOrigins)
                    db.insertFeedItems(dummyFeedItems)
                }

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = true,
                )

                homePresenter.dismissTransientError()

                assertContentState(
                    state = awaitItem(),
                    refreshing = false,
                    hasTransientError = false,
                )
            }
        }

    @Test
    fun `uiState does not emit new state when dismissTransientError is called and current state is not Content state`() =
        testScope.runTest {
            homePresenter.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())

                homePresenter.dismissTransientError()

                advanceTimeBy(1.days)

                expectNoEvents()
            }
        }

    private fun assertContentState(
        state: HomeUiState,
        refreshing: Boolean,
        hasTransientError: Boolean,
        items: List<HomeFeedItem> = dummyFeedItems.map {
            FeedItem.KotlinBlog(
                id = it.id,
                title = it.title,
                publishTime = it.publish_time,
                contentUrl = it.content_url,
                savedForLater = it.saved_for_later,
                featuredImageUrl = it.image_url.orEmpty(),
            )
        }.toHomeFeedItems(fixedClock, timeZone),
    ) {
        assertIs<HomeUiState.Content>(state)
        assertEquals(dummyFeedOrigins.size, state.selectedFeedCount)
        assertEquals(items, state.feedItems)
        assertEquals(refreshing, state.refreshing)
        assertEquals(hasTransientError, state.hasTransientError)
    }
}
