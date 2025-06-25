package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.ContentFormat
import io.github.reactivecircus.kstreamlined.kmp.database.FeedItemEntity
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Instant

class FeedItemMappersTest {

    @Test
    fun `FeedItemEntity maps to FeedItem_KotlinBlog`() {
        val entity = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog Post",
            publish_time = Instant.parse("2022-01-01T00:00:00Z"),
            content_url = "https://blog.kotlinlang.org/post",
            image_url = "https://blog.kotlinlang.org/image",
            description = null,
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            podcast_description_format = null,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val expected = FeedItem.KotlinBlog(
            id = "1",
            title = "Kotlin Blog Post",
            publishTime = Instant.parse("2022-01-01T00:00:00Z"),
            contentUrl = "https://blog.kotlinlang.org/post",
            savedForLater = false,
            featuredImageUrl = "https://blog.kotlinlang.org/image",
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity maps to FeedItem_KotlinYouTube`() {
        val entity = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedOrigin.Key.KotlinYouTubeChannel.name,
            title = "Kotlin YouTube Video",
            publish_time = Instant.parse("2022-01-02T00:00:00Z"),
            content_url = "https://youtube.com/kotlinvideo",
            image_url = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
            issue_number = null,
            podcast_audio_url = null,
            podcast_duration = null,
            podcast_start_position = null,
            podcast_description_format = null,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val expected = FeedItem.KotlinYouTube(
            id = "1",
            title = "Kotlin YouTube Video",
            publishTime = Instant.parse("2022-01-02T00:00:00Z"),
            contentUrl = "https://youtube.com/kotlinvideo",
            savedForLater = false,
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity maps to FeedItem_TalkingKotlin`() {
        val entity = FeedItemEntity(
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
            podcast_start_position = 30_000,
            podcast_description_format = null,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val expected = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin Episode",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio.mp3",
            thumbnailUrl = "image-url",
            summary = "Desc",
            summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
            summaryPlainText = null,
            duration = "35min.",
            startPositionMillis = 30_000,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity with podcast_start_position = null maps to FeedItem_TalkingKotlin with startPositionMillis = 0`() {
        val entity = FeedItemEntity(
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
            podcast_description_format = null,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val expected = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin Episode",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio.mp3",
            thumbnailUrl = "image-url",
            summary = "Desc",
            summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
            summaryPlainText = null,
            duration = "35min.",
            startPositionMillis = 0,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity maps to FeedItem_TalkingKotlin when content format is plain text`() {
        val entity = FeedItemEntity(
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
            podcast_start_position = 30_000,
            podcast_description_format = ContentFormat.Text,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val expected = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin Episode",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio.mp3",
            thumbnailUrl = "image-url",
            summary = "Desc",
            summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
            summaryPlainText = null,
            duration = "35min.",
            startPositionMillis = 30_000,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity maps to FeedItem_TalkingKotlin when content format is HTML`() {
        val entity = FeedItemEntity(
            id = "1",
            feed_origin_key = FeedOrigin.Key.TalkingKotlinPodcast.name,
            title = "Talking Kotlin Episode",
            publish_time = Instant.parse("2022-01-03T00:00:00Z"),
            content_url = "content-url",
            image_url = "image-url",
            description = "<p>Desc</p>",
            issue_number = null,
            podcast_audio_url = "audio.mp3",
            podcast_duration = "35min.",
            podcast_start_position = 30_000,
            podcast_description_format = ContentFormat.Html,
            podcast_description_plain_text = "Desc",
            saved_for_later = false,
        )
        val expected = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin Episode",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio.mp3",
            thumbnailUrl = "image-url",
            summary = "<p>Desc</p>",
            summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Html,
            summaryPlainText = "Desc",
            duration = "35min.",
            startPositionMillis = 30_000,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedItemEntity maps to FeedItem_KotlinWeekly`() {
        val entity = FeedItemEntity(
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
        val expected = FeedItem.KotlinWeekly(
            id = "1",
            title = "Kotlin Weekly Issue",
            publishTime = Instant.parse("2022-01-04T00:00:00Z"),
            contentUrl = "content-url",
            savedForLater = false,
            issueNumber = 100,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `throws error when mapping to FeedItemEntity with unknown key`() {
        val entity = FeedItemEntity(
            id = "1",
            feed_origin_key = "UnknownKey",
            title = "Unknown",
            publish_time = Instant.parse("2022-01-01T00:00:00Z"),
            content_url = "content-url",
            image_url = "image-url",
            description = "Desc",
            issue_number = null,
            podcast_audio_url = "audio.mp3",
            podcast_duration = "35min.",
            podcast_start_position = 30_000,
            podcast_description_format = null,
            podcast_description_plain_text = null,
            saved_for_later = false,
        )
        val error = assertFailsWith<IllegalStateException> {
            entity.asExternalModel()
        }
        assertEquals("Unknown feed origin key: UnknownKey", error.message)
    }
}
