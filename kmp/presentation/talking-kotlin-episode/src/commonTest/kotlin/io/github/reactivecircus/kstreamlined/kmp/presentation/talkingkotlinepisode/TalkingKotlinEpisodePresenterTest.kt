package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.ContentFormat
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
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class TalkingKotlinEpisodePresenterTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val timeZone = TimeZone.UTC

    private val dummyFeedItem = FeedItemEntity(
        id = "1",
        feed_origin_key = FeedOrigin.Key.TalkingKotlinPodcast.name,
        title = "Talking Kotlin Episode",
        publish_time = Instant.parse("2022-01-03T00:00:00Z"),
        content_url = "content-url",
        image_url = "image-url",
        description = "Desc",
        issue_number = null,
        podcast_audio_url = "audio.mp3",
        podcast_duration = "35min.",
        podcast_start_position = null,
        podcast_description_format = ContentFormat.Text,
        podcast_description_plain_text = null,
        saved_for_later = false,
    )

    private val item = dummyFeedItem.let {
        FeedItem.TalkingKotlin(
            id = it.id,
            title = it.title,
            publishTime = it.publish_time,
            contentUrl = it.content_url,
            savedForLater = it.saved_for_later,
            audioUrl = it.podcast_audio_url!!,
            thumbnailUrl = it.image_url.orEmpty(),
            summary = it.description.orEmpty(),
            summaryFormat = when (it.podcast_description_format) {
                ContentFormat.Text -> FeedItem.TalkingKotlin.ContentFormat.Text
                ContentFormat.Html -> FeedItem.TalkingKotlin.ContentFormat.Html
                null -> FeedItem.TalkingKotlin.ContentFormat.Text
            },
            summaryPlainText = it.podcast_description_plain_text,
            duration = it.podcast_duration.orEmpty(),
            startPositionMillis = it.podcast_start_position ?: 0,
        )
    }

    private val presenter = TalkingKotlinEpisodePresenter(
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Content state when LoadEpisode event is dispatched and item exists`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(TalkingKotlinEpisodeUiState.Initializing, awaitItem())

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode(dummyFeedItem.id))

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits NotFound state when LoadContent event is dispatched and item does not exist`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(TalkingKotlinEpisodeUiState.Initializing, awaitItem())

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode("id"))

                assertEquals(TalkingKotlinEpisodeUiState.NotFound, awaitItem())
            }
        }

    @Test
    fun `presenter emits Content state with updated savedForLater value when ToggleSavedForLater event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(TalkingKotlinEpisodeUiState.Initializing, awaitItem())

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode(dummyFeedItem.id))

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.ToggleSavedForLater)

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.copy(savedForLater = true).asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.ToggleSavedForLater)

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.copy(savedForLater = false).asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated startPositionMillis when SaveStartPosition event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(TalkingKotlinEpisodeUiState.Initializing, awaitItem())

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode(dummyFeedItem.id))

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.SaveStartPosition(1000))

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.copy(startPositionMillis = 1000).asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated isPlaying value when TogglePlayPause event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(TalkingKotlinEpisodeUiState.Initializing, awaitItem())

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.LoadEpisode(dummyFeedItem.id))

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.TogglePlayPause)

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = true,
                    ),
                    awaitItem(),
                )

                presenter.eventSink(TalkingKotlinEpisodeUiEvent.TogglePlayPause)

                assertEquals(
                    TalkingKotlinEpisodeUiState.Content(
                        episode = item.asPresentationModel(timeZone),
                        isPlaying = false,
                    ),
                    awaitItem(),
                )
            }
        }
}
