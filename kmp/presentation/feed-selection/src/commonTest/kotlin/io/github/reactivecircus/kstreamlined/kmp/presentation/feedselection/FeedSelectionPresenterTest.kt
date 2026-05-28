package io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.capsule.testing.asMoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.database.testing.insertFeedOrigins
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.remote.FakeFeedService
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FeedSelectionPresenterTest {
    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val allFeedOrigins = listOf(
        FeedOriginEntity(
            key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = true,
        ),
        FeedOriginEntity(
            key = FeedOrigin.Key.KotlinYouTubeChannel.name,
            title = "Kotlin YouTube",
            description = "Videos from the official Kotlin YouTube channel",
            selected = true,
        ),
        FeedOriginEntity(
            key = FeedOrigin.Key.TalkingKotlinPodcast.name,
            title = "Talking Kotlin",
            description = "Podcast on Kotlin and more by JetBrains",
            selected = true,
        ),
        FeedOriginEntity(
            key = FeedOrigin.Key.KotlinWeekly.name,
            title = "Kotlin Weekly",
            description = "Weekly community Kotlin newsletter",
            selected = true,
        ),
    )

    private val presenter = FeedSelectionPresenter(
        feedDataSource = FeedDataSource(
            feedService = feedService,
            db = db,
            dbDispatcher = testDispatcher,
        ),
        moleculeContext = testDispatcher.asMoleculeContext(),
    )

    @Test
    fun `presenter emits Loading state followed by Content state with all feed origins`() =
        testScope.runTest {
            db.insertFeedOrigins(allFeedOrigins)

            presenter.states.test {
                assertEquals(FeedSelectionUiState.Loading, awaitItem())

                val content = awaitItem()
                assertIs<FeedSelectionUiState.Content>(content)
                assertEquals(4, content.feedOrigins.size)
                assertEquals(true, content.feedOrigins.all { it.selected })
            }
        }

    @Test
    fun `presenter emits updated Content state when ToggleFeedOrigin event unselects a source`() =
        testScope.runTest {
            db.insertFeedOrigins(allFeedOrigins)

            presenter.states.test {
                assertEquals(FeedSelectionUiState.Loading, awaitItem())

                val content = awaitItem()
                assertIs<FeedSelectionUiState.Content>(content)

                presenter.eventSink(FeedSelectionUiEvent.ToggleFeedOrigin(FeedOrigin.Key.KotlinBlog))

                val updated = awaitItem()
                assertIs<FeedSelectionUiState.Content>(updated)
                val kotlinBlog = updated.feedOrigins.first { it.key == FeedOrigin.Key.KotlinBlog }
                assertEquals(false, kotlinBlog.selected)
            }
        }

    @Test
    fun `presenter emits updated Content state when ToggleFeedOrigin event selects a source`() =
        testScope.runTest {
            val originsWithOneUnselected = allFeedOrigins.map {
                if (it.key == FeedOrigin.Key.KotlinWeekly.name) it.copy(selected = false) else it
            }
            db.insertFeedOrigins(originsWithOneUnselected)

            presenter.states.test {
                assertEquals(FeedSelectionUiState.Loading, awaitItem())

                val content = awaitItem()
                assertIs<FeedSelectionUiState.Content>(content)
                val kotlinWeekly = content.feedOrigins.first { it.key == FeedOrigin.Key.KotlinWeekly }
                assertEquals(false, kotlinWeekly.selected)

                presenter.eventSink(FeedSelectionUiEvent.ToggleFeedOrigin(FeedOrigin.Key.KotlinWeekly))

                val updated = awaitItem()
                assertIs<FeedSelectionUiState.Content>(updated)
                val updatedKotlinWeekly = updated.feedOrigins.first { it.key == FeedOrigin.Key.KotlinWeekly }
                assertEquals(true, updatedKotlinWeekly.selected)
            }
        }

    @Test
    fun `unselecting the last selected source selects all others`() =
        testScope.runTest {
            val originsWithOnlyOneSelected = allFeedOrigins.map {
                if (it.key == FeedOrigin.Key.KotlinBlog.name) it else it.copy(selected = false)
            }
            db.insertFeedOrigins(originsWithOnlyOneSelected)

            presenter.states.test {
                assertEquals(FeedSelectionUiState.Loading, awaitItem())

                val content = awaitItem()
                assertIs<FeedSelectionUiState.Content>(content)
                assertEquals(1, content.feedOrigins.count { it.selected })

                presenter.eventSink(FeedSelectionUiEvent.ToggleFeedOrigin(FeedOrigin.Key.KotlinBlog))

                val updated = awaitItem()
                assertIs<FeedSelectionUiState.Content>(updated)
                val kotlinBlog = updated.feedOrigins.first { it.key == FeedOrigin.Key.KotlinBlog }
                assertEquals(false, kotlinBlog.selected)
                assertEquals(3, updated.feedOrigins.count { it.selected })
            }
        }
}
