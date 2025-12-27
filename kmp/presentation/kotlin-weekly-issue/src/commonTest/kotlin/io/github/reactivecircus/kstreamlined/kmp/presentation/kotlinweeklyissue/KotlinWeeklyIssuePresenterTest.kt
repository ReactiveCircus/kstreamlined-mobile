package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeKotlinWeeklyIssueEntries
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class KotlinWeeklyIssuePresenterTest {
    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val dummyFeedItem = FeedItemEntity(
        id = "1",
        feed_origin_key = FeedOrigin.Key.KotlinWeekly.name,
        title = "Kotlin Weekly Issue",
        publish_time = Instant.parse("2022-01-04T00:00:00Z"),
        content_url = "content-url",
        image_url = "image-url",
        description = null,
        issue_number = 100,
        podcast_audio_url = null,
        podcast_duration = null,
        podcast_start_position = null,
        podcast_description_format = null,
        podcast_description_plain_text = null,
        saved_for_later = false,
    )

    private val item = dummyFeedItem.let {
        FeedItem.KotlinWeekly(
            id = it.id,
            title = it.title,
            publishTime = it.publish_time,
            contentUrl = it.content_url,
            savedForLater = it.saved_for_later,
            issueNumber = it.issue_number!!.toInt(),
        )
    }

    private val fakeKotlinWeeklyIssueItems = FakeKotlinWeeklyIssueEntries.map {
        KotlinWeeklyIssueItem(
            title = it.title,
            summary = it.summary,
            url = it.url,
            source = it.source,
            group = when (it.group) {
                KotlinWeeklyIssueEntry.Group.Announcements -> KotlinWeeklyIssueItem.Group.Announcements
                KotlinWeeklyIssueEntry.Group.Articles -> KotlinWeeklyIssueItem.Group.Articles
                KotlinWeeklyIssueEntry.Group.Android -> KotlinWeeklyIssueItem.Group.Android
                KotlinWeeklyIssueEntry.Group.Videos -> KotlinWeeklyIssueItem.Group.Videos
                KotlinWeeklyIssueEntry.Group.Libraries -> KotlinWeeklyIssueItem.Group.Libraries
            },
        )
    }

    private fun presenter(id: String = item.id) = KotlinWeeklyIssuePresenter(
        id = id,
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Content state when initialized and loading issue succeeds`() =
        testScope.runTest {
            presenter().states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = { FakeKotlinWeeklyIssueEntries }

                assertEquals(
                    KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = fakeKotlinWeeklyIssueItems.groupBy { it.group },
                        savedForLater = item.savedForLater,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Error state when initialized and loading issue fails`() =
        testScope.runTest {
            presenter().states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = {
                    error("Failed to fetch Kotlin Weekly issue")
                }

                assertEquals(KotlinWeeklyIssueUiState.Error, awaitItem())
            }
        }

    @Test
    fun `presenter emits Error state when item does not exist for the given id`() =
        testScope.runTest {
            presenter(id = "unknown-id").states.test {
                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                assertEquals(KotlinWeeklyIssueUiState.Error, awaitItem())
            }
        }

    @Test
    fun `presenter emits Content state when current state is Error and loading issue again succeeds`() =
        testScope.runTest {
            val presenter = presenter()
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = {
                    error("Failed to fetch Kotlin Weekly issue")
                }

                assertEquals(KotlinWeeklyIssueUiState.Error, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = { FakeKotlinWeeklyIssueEntries }

                presenter.eventSink(KotlinWeeklyIssueUiEvent.Refresh)

                assertEquals(
                    KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = fakeKotlinWeeklyIssueItems.groupBy { it.group },
                        savedForLater = item.savedForLater,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated savedForLater value when ToggleSavedForLater event is dispatched`() =
        testScope.runTest {
            val presenter = presenter()
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                assertEquals(
                    KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = fakeKotlinWeeklyIssueItems.groupBy { it.group },
                        savedForLater = item.savedForLater,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(KotlinWeeklyIssueUiEvent.ToggleSavedForLater)

                assertEquals(
                    KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = fakeKotlinWeeklyIssueItems.groupBy { it.group },
                        savedForLater = true,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(KotlinWeeklyIssueUiEvent.ToggleSavedForLater)

                assertEquals(
                    KotlinWeeklyIssueUiState.Content(
                        id = item.id,
                        contentUrl = item.contentUrl,
                        issueItems = fakeKotlinWeeklyIssueItems.groupBy { it.group },
                        savedForLater = false,
                    ),
                    awaitItem(),
                )
            }
        }
}
