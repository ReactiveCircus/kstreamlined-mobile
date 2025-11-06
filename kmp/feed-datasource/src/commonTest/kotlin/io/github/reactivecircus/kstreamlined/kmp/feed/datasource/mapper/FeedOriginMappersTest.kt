package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FeedOriginMappersTest {
    @Test
    fun `FeedOriginEntity maps to FeedOrigin with KotlinBlog key`() {
        val entity = FeedOriginEntity(
            key = FeedOrigin.Key.KotlinBlog.name,
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = true,
        )
        val expected = FeedOrigin(
            title = "Kotlin Blog",
            description = "Latest news from the official Kotlin Blog",
            selected = true,
            key = FeedOrigin.Key.KotlinBlog,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedOriginEntity maps to FeedOrigin with KotlinYouTubeChannel key`() {
        val entity = FeedOriginEntity(
            key = FeedOrigin.Key.KotlinYouTubeChannel.name,
            title = "Kotlin YouTube",
            description = "The official YouTube channel of the Kotlin programming language",
            selected = true,
        )
        val expected = FeedOrigin(
            title = "Kotlin YouTube",
            description = "The official YouTube channel of the Kotlin programming language",
            selected = true,
            key = FeedOrigin.Key.KotlinYouTubeChannel,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedOriginEntity maps to FeedOrigin with TalkingKotlinPodcast key`() {
        val entity = FeedOriginEntity(
            key = FeedOrigin.Key.TalkingKotlinPodcast.name,
            title = "Talking Kotlin",
            description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
            selected = true,
        )
        val expected = FeedOrigin(
            title = "Talking Kotlin",
            description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
            selected = true,
            key = FeedOrigin.Key.TalkingKotlinPodcast,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `FeedOriginEntity maps to FeedOrigin with KotlinWeekly key`() {
        val entity = FeedOriginEntity(
            key = FeedOrigin.Key.KotlinWeekly.name,
            title = "Kotlin Weekly",
            description = "Weekly community Kotlin newsletter, hosted by Enrique",
            selected = true,
        )
        val expected = FeedOrigin(
            title = "Kotlin Weekly",
            description = "Weekly community Kotlin newsletter, hosted by Enrique",
            selected = true,
            key = FeedOrigin.Key.KotlinWeekly,
        )
        assertEquals(expected, entity.asExternalModel())
    }

    @Test
    fun `throws error when mapping FeedOriginEntity with unknown key`() {
        val entity = FeedOriginEntity(
            key = "UnknownKey",
            title = "Unknown",
            description = "Unknown",
            selected = true,
        )
        val error = assertFailsWith<IllegalStateException> {
            entity.asExternalModel()
        }
        assertEquals("Unknown feed origin key: UnknownKey", error.message)
    }
}
