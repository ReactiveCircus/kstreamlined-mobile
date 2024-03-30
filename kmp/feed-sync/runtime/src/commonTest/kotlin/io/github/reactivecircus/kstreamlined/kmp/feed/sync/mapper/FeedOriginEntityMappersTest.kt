package io.github.reactivecircus.kstreamlined.kmp.feed.sync.mapper

import io.github.reactivecircus.kstreamlined.kmp.database.FeedOriginEntity
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FeedOriginEntityMappersTest {

    @Test
    fun `list of FeedOriginEntity maps to expected network models when selected feed origins exist`() {
        val feedOriginEntities = listOf(
            FeedOriginEntity(
                key = FeedSource.Key.KotlinBlog.name,
                title = "title1",
                description = "description1",
                selected = true,
            ),
            FeedOriginEntity(
                key = FeedSource.Key.KotlinWeekly.name,
                title = "title2",
                description = "description2",
                selected = false,
            ),
            FeedOriginEntity(
                key = FeedSource.Key.TalkingKotlinPodcast.name,
                title = "title3",
                description = "description3",
                selected = true,
            )
        )
        val expected = listOf(
            FeedSource.Key.KotlinBlog,
            FeedSource.Key.TalkingKotlinPodcast,
        )
        assertEquals(expected, feedOriginEntities.asNetworkModels())
    }

    @Test
    fun `list of FeedOriginEntity maps to null when no selected feed origins exist`() {
        assertNull(emptyList<FeedOriginEntity>().asNetworkModels())

        val feedOriginEntities = listOf(
            FeedOriginEntity(
                key = FeedSource.Key.KotlinBlog.name,
                title = "title1",
                description = "description1",
                selected = false,
            ),
        )
        assertNull(feedOriginEntities.asNetworkModels())
    }

    @Test
    fun `FeedSource maps to expected FeedOriginEntity when current feed origins exist`() {
        val feedSource = FeedSource(
            key = FeedSource.Key.KotlinBlog,
            title = "title1",
            description = "description1",
        )
        val currentFeedOrigins = listOf(
            FeedOriginEntity(
                key = FeedSource.Key.KotlinBlog.name,
                title = "title1",
                description = "description1",
                selected = true,
            ),
        )
        val expected = FeedOriginEntity(
            key = FeedSource.Key.KotlinBlog.name,
            title = "title1",
            description = "description1",
            selected = true,
        )
        assertEquals(expected, feedSource.toDbModel(currentFeedOrigins))
    }

    @Test
    fun `FeedSource maps to expected FeedOriginEntity when current feed origins do not exist`() {
        val feedSource = FeedSource(
            key = FeedSource.Key.KotlinBlog,
            title = "title1",
            description = "description1",
        )
        val expected = FeedOriginEntity(
            key = FeedSource.Key.KotlinBlog.name,
            title = "title1",
            description = "description1",
            selected = true,
        )
        assertEquals(expected, feedSource.toDbModel(emptyList()))
    }

    @Test
    fun `list of FeedOriginEntity maps to expected sync params`() {
        assertEquals("", emptyList<FeedOriginEntity>().toSyncParams())

        val feedOriginEntities = listOf(
            FeedOriginEntity(
                key = FeedSource.Key.TalkingKotlinPodcast.name,
                title = "title1",
                description = "description1",
                selected = true,
            ),
            FeedOriginEntity(
                key = FeedSource.Key.KotlinWeekly.name,
                title = "title2",
                description = "description2",
                selected = false,
            ),
            FeedOriginEntity(
                key = FeedSource.Key.KotlinBlog.name,
                title = "title3",
                description = "description3",
                selected = true,
            )
        )
        val expected = "KotlinBlog,TalkingKotlinPodcast"
        assertEquals(expected, feedOriginEntities.toSyncParams())
    }
}
