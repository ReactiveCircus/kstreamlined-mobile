package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedItemEntityMappersTest {

    @Test
    fun `FeedEntry_KotlinBlog maps to expected db model`() {
        val feedEntry = FeedEntry.KotlinBlog(
            id = "1",
            title = "Kotlin Blog Post",
            publishTime = Instant.parse("2022-01-01T00:00:00Z"),
            contentUrl = "https://blog.kotlinlang.org/post",
            featuredImageUrl = "https://blog.kotlinlang.org/image",
        )
        val expected = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedSource.Key.KotlinBlog.name,
            title = "Kotlin Blog Post",
            publish_time = Instant.parse("2022-01-01T00:00:00Z"),
            content_url = "https://blog.kotlinlang.org/post",
            image_url = "https://blog.kotlinlang.org/image",
            description = null,
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = false,
        )
        assertEquals(expected, feedEntry.toDbModel(emptyList()))
    }

    @Test
    fun `FeedEntry_KotlinYouTube maps to expected db model`() {
        val feedEntry = FeedEntry.KotlinYouTube(
            id = "1",
            title = "Kotlin YouTube Video",
            publishTime = Instant.parse("2022-01-02T00:00:00Z"),
            contentUrl = "https://youtube.com/kotlinvideo",
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
        )
        val expected = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedSource.Key.KotlinYouTubeChannel.name,
            title = "Kotlin YouTube Video",
            publish_time = Instant.parse("2022-01-02T00:00:00Z"),
            content_url = "https://youtube.com/kotlinvideo",
            image_url = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = false,
        )
        assertEquals(expected, feedEntry.toDbModel(emptyList()))
    }

    @Test
    fun `FeedEntry_TalkingKotlin maps to expected db model`() {
        val feedEntry = FeedEntry.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin Podcast",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "https://talkingkotlin.com/episode",
            audioUrl = "audio.mp3",
            thumbnailUrl = "https://talkingkotlin.com/podcast/logo",
            summary = "Talking kotlin podcast episode summary",
            duration = "35min.",
        )
        val existingEntity = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedSource.Key.TalkingKotlinPodcast.name,
            title = "Title",
            publish_time = Instant.parse("2022-01-03T00:00:00Z"),
            content_url = "url",
            image_url = "image-url",
            description = "desc",
            issue_number = null,
            podcast_audio_url = "audio.mp3",
            podcast_duration = "35min.",
            podcast_start_position = 30_000,
            saved_for_later = true,
        )
        val expected = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedSource.Key.TalkingKotlinPodcast.name,
            title = "Talking Kotlin Podcast",
            publish_time = Instant.parse("2022-01-03T00:00:00Z"),
            content_url = "https://talkingkotlin.com/episode",
            image_url = "https://talkingkotlin.com/podcast/logo",
            description = "Talking kotlin podcast episode summary",
            issue_number = null,
            podcast_audio_url = "audio.mp3",
            podcast_duration = "35min.",
            podcast_start_position = 30_000,
            saved_for_later = true,
        )
        assertEquals(expected, feedEntry.toDbModel(listOf(existingEntity)))
    }

    @Test
    fun `FeedEntry_KotlinWeekly maps to expected db model`() {
        val feedEntry = FeedEntry.KotlinWeekly(
            id = "1",
            title = "Kotlin Weekly #1",
            publishTime = Instant.parse("2022-01-04T00:00:00Z"),
            contentUrl = "https://kotlinweekly.net/issue1",
            issueNumber = 1,
        )
        val expected = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedSource.Key.KotlinWeekly.name,
            title = "Kotlin Weekly #1",
            publish_time = Instant.parse("2022-01-04T00:00:00Z"),
            content_url = "https://kotlinweekly.net/issue1",
            image_url = null,
            description = null,
            issue_number = 1,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            saved_for_later = false,
        )
        assertEquals(expected, feedEntry.toDbModel(emptyList()))
    }
}
