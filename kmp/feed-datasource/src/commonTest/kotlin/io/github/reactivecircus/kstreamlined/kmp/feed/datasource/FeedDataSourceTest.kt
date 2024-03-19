package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.database.testing.createInMemoryDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.networking.FakeFeedService
import io.github.reactivecircus.kstreamlined.kmp.networking.FakeKotlinWeeklyIssueEntries
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class FeedDataSourceTest {

    private val feedService = FakeFeedService()

    private val db = createInMemoryDatabase()

    private val testDispatcher = StandardTestDispatcher()

    private val feedDataSource = FeedDataSource(
        feedService = feedService,
        db = db,
        dbDispatcher = testDispatcher,
    )

    @Test
    fun `streamFeedOrigins emits when feed origins change in DB`() = runTest(testDispatcher) {
        feedDataSource.streamFeedOrigins().test {
            assertEquals(emptyList(), awaitItem())

            db.transaction {
                db.feedOriginEntityQueries.upsertFeedOrigin(
                    key = FeedOrigin.Key.KotlinBlog.name,
                    title = "Kotlin Blog",
                    description = "Latest news from the official Kotlin Blog",
                    selected = true,
                )
                db.feedOriginEntityQueries.upsertFeedOrigin(
                    key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                    title = "Kotlin YouTube",
                    description = "The official YouTube channel of the Kotlin programming language",
                    selected = true,
                )
            }

            assertEquals(
                listOf(
                    FeedOrigin(
                        key = FeedOrigin.Key.KotlinBlog,
                        title = "Kotlin Blog",
                        description = "Latest news from the official Kotlin Blog",
                        selected = true,
                    ),
                    FeedOrigin(
                        key = FeedOrigin.Key.KotlinYouTubeChannel,
                        title = "Kotlin YouTube",
                        description = "The official YouTube channel of the Kotlin programming language",
                        selected = true,
                    )
                ),
                awaitItem()
            )

            db.feedOriginEntityQueries.updateSelection(
                selected = false,
                key = FeedOrigin.Key.KotlinBlog.name,
            )
            assertEquals(
                listOf(
                    FeedOrigin(
                        key = FeedOrigin.Key.KotlinBlog,
                        title = "Kotlin Blog",
                        description = "Latest news from the official Kotlin Blog",
                        selected = false,
                    ),
                    FeedOrigin(
                        key = FeedOrigin.Key.KotlinYouTubeChannel,
                        title = "Kotlin YouTube",
                        description = "The official YouTube channel of the Kotlin programming language",
                        selected = true,
                    )
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `streamFeedItemsForSelectedOrigins emits when queried feed items change in DB`() =
        runTest(testDispatcher) {
            feedDataSource.streamFeedItemsForSelectedOrigins().test {
                assertEquals(emptyList(), awaitItem())

                db.transaction {
                    db.feedOriginEntityQueries.upsertFeedOrigin(
                        key = FeedOrigin.Key.KotlinBlog.name,
                        title = "Kotlin Blog",
                        description = "Latest news from the official Kotlin Blog",
                        selected = true,
                    )
                    db.feedOriginEntityQueries.upsertFeedOrigin(
                        key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                        title = "Kotlin YouTube",
                        description = "The official YouTube channel of the Kotlin programming language",
                        selected = true,
                    )
                    db.feedItemEntityQueries.upsertFeedItem(
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
                    db.feedItemEntityQueries.upsertFeedItem(
                        id = "2",
                        feed_origin_key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                        title = "Kotlin YouTube Video",
                        publish_time = "2022-01-02T00:00:00Z".toInstant(),
                        content_url = "content-url-2",
                        image_url = "image-url-2",
                        description = "Desc",
                        issue_number = null,
                        podcast_audio_url = null,
                        podcast_duration = null,
                        podcast_start_position = null,
                        saved_for_later = false,
                    )
                }

                assertEquals(
                    listOf(
                        FeedItem.KotlinYouTube(
                            id = "2",
                            title = "Kotlin YouTube Video",
                            publishTime = "2022-01-02T00:00:00Z".toInstant(),
                            contentUrl = "content-url-2",
                            savedForLater = false,
                            thumbnailUrl = "image-url-2",
                            description = "Desc",
                        ),
                        FeedItem.KotlinBlog(
                            id = "1",
                            title = "Kotlin Blog Post",
                            publishTime = "2022-01-01T00:00:00Z".toInstant(),
                            contentUrl = "content-url-1",
                            savedForLater = false,
                            featuredImageUrl = "image-url-1",
                        ),
                    ),
                    awaitItem(),
                )

                db.feedOriginEntityQueries.updateSelection(
                    selected = false,
                    key = FeedOrigin.Key.KotlinBlog.name,
                )

                assertEquals(
                    listOf(
                        FeedItem.KotlinYouTube(
                            id = "2",
                            title = "Kotlin YouTube Video",
                            publishTime = "2022-01-02T00:00:00Z".toInstant(),
                            contentUrl = "content-url-2",
                            savedForLater = false,
                            thumbnailUrl = "image-url-2",
                            description = "Desc",
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `streamSavedFeedItems emits when saved feed items change in DB`() =
        runTest(testDispatcher) {
            feedDataSource.streamSavedFeedItems().test {
                assertEquals(emptyList(), awaitItem())

                db.transaction {
                    db.feedOriginEntityQueries.upsertFeedOrigin(
                        key = FeedOrigin.Key.KotlinBlog.name,
                        title = "Kotlin Blog",
                        description = "Latest news from the official Kotlin Blog",
                        selected = true,
                    )
                    db.feedOriginEntityQueries.upsertFeedOrigin(
                        key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                        title = "Kotlin YouTube",
                        description = "The official YouTube channel of the Kotlin programming language",
                        selected = true,
                    )
                    db.feedItemEntityQueries.upsertFeedItem(
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
                    db.feedItemEntityQueries.upsertFeedItem(
                        id = "2",
                        feed_origin_key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                        title = "Kotlin YouTube Video",
                        publish_time = "2022-01-02T00:00:00Z".toInstant(),
                        content_url = "content-url-2",
                        image_url = "image-url-2",
                        description = "Desc",
                        issue_number = null,
                        podcast_audio_url = null,
                        podcast_duration = null,
                        podcast_start_position = null,
                        saved_for_later = false,
                    )
                }

                assertEquals(emptyList(), awaitItem())

                db.feedItemEntityQueries.updateSavedForLaterById(
                    saved_for_later = true,
                    id = "1",
                )

                assertEquals(
                    listOf(
                        FeedItem.KotlinBlog(
                            id = "1",
                            title = "Kotlin Blog Post",
                            publishTime = "2022-01-01T00:00:00Z".toInstant(),
                            contentUrl = "content-url-1",
                            savedForLater = true,
                            featuredImageUrl = "image-url-1",
                        ),
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `streamFeedItemById emits when feed item changes in DB`() = runTest(testDispatcher) {
        feedDataSource.streamFeedItemById("id").test {
            assertNull(awaitItem())

            db.feedItemEntityQueries.upsertFeedItem(
                id = "id",
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

            assertEquals(
                FeedItem.KotlinBlog(
                    id = "id",
                    title = "Kotlin Blog Post",
                    publishTime = "2022-01-01T00:00:00Z".toInstant(),
                    contentUrl = "content-url-1",
                    savedForLater = false,
                    featuredImageUrl = "image-url-1",
                ),
                awaitItem(),
            )

            db.feedItemEntityQueries.upsertFeedItem(
                id = "id",
                feed_origin_key = FeedOrigin.Key.KotlinBlog.name,
                title = "Kotlin Blog Post 2",
                publish_time = "2022-01-01T00:00:00Z".toInstant(),
                content_url = "content-url-2",
                image_url = "image-url-2",
                description = null,
                issue_number = null,
                podcast_audio_url = null,
                podcast_duration = null,
                podcast_start_position = null,
                saved_for_later = true,
            )

            assertEquals(
                FeedItem.KotlinBlog(
                    id = "id",
                    title = "Kotlin Blog Post 2",
                    publishTime = "2022-01-01T00:00:00Z".toInstant(),
                    contentUrl = "content-url-2",
                    savedForLater = true,
                    featuredImageUrl = "image-url-2",
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `loadKotlinWeeklyIssues returns result when fetching from service succeeds`() = runTest {
        feedService.nextKotlinWeeklyIssueResponse = {
            FakeKotlinWeeklyIssueEntries
        }

        val expected = FakeKotlinWeeklyIssueEntries.map { it.asExternalModel() }
        val actual = feedDataSource.loadKotlinWeeklyIssue("url")

        assertEquals(expected, actual)
    }

    @Test
    fun `loadKotlinWeeklyIssues throws exception when fetching from service fails`() = runTest {
        feedService.nextKotlinWeeklyIssueResponse = {
            error("Failed to fetch Kotlin Weekly issue")
        }

        assertFailsWith<IllegalStateException> {
            feedDataSource.loadKotlinWeeklyIssue("url")
        }
    }

    @Test
    fun `selectFeedSource updates feed origin selection in DB`() = runTest(testDispatcher) {
        db.feedOriginEntityQueries.upsertFeedOrigin(
            key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = false,
        )

        feedDataSource.selectFeedSource(FeedOrigin.Key.KotlinBlog)

        assertEquals(
            listOf(
                FeedOriginEntity(
                    key = FeedOrigin.Key.KotlinBlog.name,
                    title = "Kotlin Blog",
                    description = "Latest news from the official Kotlin Blog",
                    selected = true,
                )
            ),
            db.feedOriginEntityQueries.allFeedOrigins().executeAsList(),
        )
    }

    @Test
    fun `unselectFeedSource updates feed origin selection in DB`() = runTest(testDispatcher) {
        db.feedOriginEntityQueries.upsertFeedOrigin(
            key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = true,
        )

        feedDataSource.unselectFeedSource(FeedOrigin.Key.KotlinBlog)

        assertEquals(
            listOf(
                FeedOriginEntity(
                    key = FeedOrigin.Key.KotlinBlog.name,
                    title = "Kotlin Blog",
                    description = "Latest news from the official Kotlin Blog",
                    selected = false,
                )
            ),
            db.feedOriginEntityQueries.allFeedOrigins().executeAsList(),
        )
    }

    @Test
    fun `unselectFeedSource selects all other feed origin in DB when the only selected one is being unselected`() =
        runTest(testDispatcher) {
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = FeedOrigin.Key.KotlinBlog.name,
                title = "Kotlin Blog",
                description = "Latest news from the official Kotlin Blog",
                selected = false,
            )
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                title = "Kotlin YouTube",
                description = "The official YouTube channel of the Kotlin programming language",
                selected = true,
            )
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = FeedOrigin.Key.TalkingKotlinPodcast.name,
                title = "Talking Kotlin",
                description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
                selected = false,
            )
            db.feedOriginEntityQueries.upsertFeedOrigin(
                key = FeedOrigin.Key.KotlinWeekly.name,
                title = "Kotlin Weekly",
                description = "Weekly community Kotlin newsletter, hosted by Enrique",
                selected = false,
            )

            feedDataSource.unselectFeedSource(FeedOrigin.Key.KotlinYouTubeChannel)

            assertEquals(
                listOf(
                    FeedOriginEntity(
                        key = FeedOrigin.Key.KotlinBlog.name,
                        title = "Kotlin Blog",
                        description = "Latest news from the official Kotlin Blog",
                        selected = true,
                    ),
                    FeedOriginEntity(
                        key = FeedOrigin.Key.KotlinYouTubeChannel.name,
                        title = "Kotlin YouTube",
                        description = "The official YouTube channel of the Kotlin programming language",
                        selected = false,
                    ),
                    FeedOriginEntity(
                        key = FeedOrigin.Key.TalkingKotlinPodcast.name,
                        title = "Talking Kotlin",
                        description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
                        selected = true,
                    ),
                    FeedOriginEntity(
                        key = FeedOrigin.Key.KotlinWeekly.name,
                        title = "Kotlin Weekly",
                        description = "Weekly community Kotlin newsletter, hosted by Enrique",
                        selected = true,
                    ),
                ),
                db.feedOriginEntityQueries.allFeedOrigins().executeAsList(),
            )
        }

    @Test
    fun `addSavedFeedItem updates saved_for_later for item in DB`() = runTest(testDispatcher) {
        db.feedItemEntityQueries.upsertFeedItem(
            id = "1",
            feed_origin_key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog Post",
            publish_time = "2022-01-01T00:00:00Z".toInstant(),
            content_url = "https://blog.kotlinlang.org/post",
            image_url = "https://blog.kotlinlang.org/image",
            description = null,
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = false,
        )

        feedDataSource.addSavedFeedItem("1")

        assertEquals(
            true,
            db.feedItemEntityQueries.feedItemById("1").executeAsOne().saved_for_later,
        )
    }

    @Test
    fun `removeSavedFeedItem updates saved_for_later for item in DB`() = runTest(testDispatcher) {
        db.feedItemEntityQueries.upsertFeedItem(
            id = "1",
            feed_origin_key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog Post",
            publish_time = "2022-01-01T00:00:00Z".toInstant(),
            content_url = "https://blog.kotlinlang.org/post",
            image_url = "https://blog.kotlinlang.org/image",
            description = null,
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = true,
        )

        feedDataSource.removeSavedFeedItem("1")

        assertEquals(
            false,
            db.feedItemEntityQueries.feedItemById("1").executeAsOne().saved_for_later,
        )
    }

    @Test
    fun `saveTalkingKotlinEpisodeStartPosition updates podcast_start_position for item in DB`() =
        runTest(testDispatcher) {
            db.feedItemEntityQueries.upsertFeedItem(
                id = "1",
                feed_origin_key = FeedOrigin.Key.TalkingKotlinPodcast.name,
                title = "Talking Kotlin Episode",
                publish_time = "2022-01-03T00:00:00Z".toInstant(),
                content_url = "content-url",
                image_url = "image-url",
                description = "Desc",
                issue_number = null,
                podcast_audio_url = "audio.mp3",
                podcast_duration = "35min.",
                podcast_start_position = null,
                saved_for_later = false,
            )

            feedDataSource.saveTalkingKotlinEpisodeStartPosition("1", 30_000)

            assertEquals(
                30_000,
                db.feedItemEntityQueries.feedItemById("1").executeAsOne().podcast_start_position,
            )
        }
}
