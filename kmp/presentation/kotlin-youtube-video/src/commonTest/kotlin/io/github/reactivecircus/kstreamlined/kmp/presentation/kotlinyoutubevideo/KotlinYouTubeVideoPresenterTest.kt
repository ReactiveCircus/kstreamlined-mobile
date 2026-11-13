package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.capsule.testing.asMoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedItems
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class KotlinYouTubeVideoPresenterTest {
    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val timeZone = TimeZone.UTC

    private val dummyFeedItem = FeedItemEntity(
        id = "yt:video:mQqwAmQBkss",
        feed_origin_key = FeedOrigin.Key.KotlinYouTubeChannel.name,
        title = "Kotlin YouTube Video",
        publish_time = Instant.parse("2022-01-03T00:00:00Z"),
        content_url = "https://www.youtube.com/watch?v=mQqwAmQBkss",
        image_url = "image-url",
        description = "Desc",
        issue_number = null,
        podcast_audio_url = null,
        podcast_duration = null,
        podcast_start_position = null,
        podcast_description_format = null,
        podcast_description_plain_text = null,
        saved_for_later = false,
        supports_tldr = null,
        video_id = "mQqwAmQBkss",
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
            videoId = it.video_id!!,
        )
    }

    private fun presenter(id: String = item.id) = KotlinYouTubeVideoPresenter(
        id = id,
        feedDataSource = FeedDataSource(
            db = db,
            dbDispatcher = testDispatcher,
        ),
        timeZone = timeZone,
        moleculeContext = testDispatcher.asMoleculeContext(),
    )

    @Test
    fun `presenter emits Content state when initialized and item exists`() =
        testScope.runTest {
            presenter().states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(KotlinYouTubeVideoUiState.Initializing, awaitItem())

                assertEquals(
                    KotlinYouTubeVideoUiState.Content(
                        video = item.asPresentationModel(timeZone = timeZone),
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits NotFound state when initialized and item does not exist`() =
        testScope.runTest {
            presenter("unknown-id").states.test {
                assertEquals(KotlinYouTubeVideoUiState.Initializing, awaitItem())

                assertEquals(KotlinYouTubeVideoUiState.NotFound, awaitItem())
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

                assertEquals(KotlinYouTubeVideoUiState.Initializing, awaitItem())

                assertEquals(
                    KotlinYouTubeVideoUiState.Content(
                        video = item.asPresentationModel(timeZone = timeZone),
                    ),
                    awaitItem(),
                )

                presenter.eventSink(KotlinYouTubeVideoUiEvent.ToggleSavedForLater)

                assertEquals(
                    KotlinYouTubeVideoUiState.Content(
                        video = item.copy(savedForLater = true).asPresentationModel(timeZone = timeZone),
                    ),
                    awaitItem(),
                )

                presenter.eventSink(KotlinYouTubeVideoUiEvent.ToggleSavedForLater)

                assertEquals(
                    KotlinYouTubeVideoUiState.Content(
                        video = item.copy(savedForLater = false).asPresentationModel(timeZone = timeZone),
                    ),
                    awaitItem(),
                )
            }
        }
}
