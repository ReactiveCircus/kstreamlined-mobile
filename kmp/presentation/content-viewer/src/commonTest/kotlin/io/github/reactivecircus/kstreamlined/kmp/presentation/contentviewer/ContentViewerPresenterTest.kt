package io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class ContentViewerPresenterTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val dummyFeedItem = FeedItemEntity(
        id = "1",
        feed_origin_key = FeedOrigin.Key.KotlinYouTubeChannel.name,
        title = "Kotlin YouTube Video",
        publish_time = Instant.parse("2022-01-02T00:00:00Z"),
        content_url = "content-url-2",
        image_url = "image-url-2",
        description = "Desc",
        issue_number = null,
        podcast_audio_url = null,
        podcast_duration = null,
        podcast_start_position = null,
        podcast_description_format = null,
        podcast_description_plain_text = null,
        saved_for_later = false,
    )

    private val item = dummyFeedItem.let {
        FeedItem.KotlinYouTube(
            id = it.id,
            title = it.title,
            publishTime = it.publish_time,
            contentUrl = it.content_url,
            savedForLater = it.saved_for_later,
            thumbnailUrl = it.image_url.orEmpty(),
            description = it.description.orEmpty(),
        )
    }

    private val presenter = ContentViewerPresenter(
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Content state when LoadContent event is dispatched and item exists`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(ContentViewerUiState.Initializing, awaitItem())

                presenter.eventSink(ContentViewerUiEvent.LoadContent(dummyFeedItem.id))

                assertEquals(ContentViewerUiState.Content(item), awaitItem())
            }
        }

    @Test
    fun `presenter emits NotFound state when LoadContent event is dispatched and item does not exist`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(ContentViewerUiState.Initializing, awaitItem())

                presenter.eventSink(ContentViewerUiEvent.LoadContent("id"))

                assertEquals(ContentViewerUiState.NotFound, awaitItem())
            }
        }

    @Test
    fun `presenter emits Content state with updated savedForLater value when ToggleSavedForLater event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(ContentViewerUiState.Initializing, awaitItem())

                presenter.eventSink(ContentViewerUiEvent.LoadContent(dummyFeedItem.id))

                assertEquals(ContentViewerUiState.Content(item), awaitItem())

                presenter.eventSink(ContentViewerUiEvent.ToggleSavedForLater)

                assertEquals(
                    ContentViewerUiState.Content(item.copy(savedForLater = true)),
                    awaitItem()
                )

                presenter.eventSink(ContentViewerUiEvent.ToggleSavedForLater)

                assertEquals(
                    ContentViewerUiState.Content(item.copy(savedForLater = false)),
                    awaitItem()
                )
            }
        }
}
