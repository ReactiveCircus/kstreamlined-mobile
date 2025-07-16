package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeKotlinWeeklyIssueEntries
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
                Announcements -> Announcements
                Articles -> Articles
                Android -> Android
                Videos -> Videos
                Libraries -> Libraries
            },
        )
    }

    private val presenter = KotlinWeeklyIssuePresenter(
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Content state when LoadIssue event is dispatched and loading issue succeeds`() {
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = { FakeKotlinWeeklyIssueEntries }

                presenter.eventSink(KotlinWeeklyIssueUiEvent.LoadIssue(dummyFeedItem.id))

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
    }

    @Test
    fun `presenter emits Failed state when LoadIssue event is dispatched and loading issue fails`() {
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                feedService.nextKotlinWeeklyIssueResponse = {
                    error("Failed to fetch Kotlin Weekly issue")
                }

                presenter.eventSink(KotlinWeeklyIssueUiEvent.LoadIssue(dummyFeedItem.id))

                assertEquals(KotlinWeeklyIssueUiState.Failed, awaitItem())
            }
        }
    }

    @Test
    fun `presenter emits Failed state when LoadIssue event is dispatched and item does not exist`() {
        testScope.runTest {
            presenter.states.test {
                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                presenter.eventSink(KotlinWeeklyIssueUiEvent.LoadIssue("id"))

                assertEquals(KotlinWeeklyIssueUiState.Failed, awaitItem())
            }
        }
    }

    @Test
    fun `presenter emits Content state with updated savedForLater value when ToggleSavedForLater event is dispatched`() {
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinWeeklyIssueUiState.Loading, awaitItem())

                presenter.eventSink(KotlinWeeklyIssueUiEvent.LoadIssue(dummyFeedItem.id))

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
}
