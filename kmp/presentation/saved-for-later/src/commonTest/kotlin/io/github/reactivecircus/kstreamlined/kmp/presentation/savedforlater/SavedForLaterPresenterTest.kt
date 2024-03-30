package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class SavedForLaterPresenterTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val timeZone = TimeZone.UTC

    private val dummyFeedItem = FeedItemEntity(
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
        saved_for_later = true,
    )

    private val item = dummyFeedItem.let {
        FeedItem.KotlinBlog(
            id = it.id,
            title = it.title,
            publishTime = it.publish_time,
            contentUrl = it.content_url,
            savedForLater = it.saved_for_later,
            featuredImageUrl = it.image_url.orEmpty(),
        )
    }

    private val savedForLaterPresenter = SavedForLaterPresenter(
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        scope = testScope.backgroundScope,
    )

    @Test
    fun `uiState emits Content state with saved feed items when datasource emits new items`() =
        testScope.runTest {
            savedForLaterPresenter.uiState.test {
                assertEquals(SavedForLaterUiState.Content(emptyList()), awaitItem())

                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(
                    SavedForLaterUiState.Content(
                        listOf(item).toSavedForLaterFeedItems(timeZone)
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `uiState emits Content state with removed item when removeSavedItem is called`() =
        testScope.runTest {
            savedForLaterPresenter.uiState.test {
                assertEquals(SavedForLaterUiState.Content(emptyList()), awaitItem())

                db.transaction {
                    db.insertFeedItems(listOf(dummyFeedItem))
                }

                assertEquals(
                    SavedForLaterUiState.Content(
                        listOf(item).toSavedForLaterFeedItems(timeZone)
                    ),
                    awaitItem(),
                )

                savedForLaterPresenter.removeSavedItem(item.id)

                assertEquals(SavedForLaterUiState.Content(emptyList()), awaitItem())
            }
        }
}
