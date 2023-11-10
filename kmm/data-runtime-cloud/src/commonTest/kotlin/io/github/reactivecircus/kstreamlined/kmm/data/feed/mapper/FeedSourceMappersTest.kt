package io.github.reactivecircus.kstreamlined.kmm.data.feed.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedSource.Key
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FeedSourceMappersTest {

    @Test
    fun `FeedSource_Key maps to expected FeedSourceKey`() {
        assertEquals(FeedSourceKey.KOTLIN_BLOG, FeedSource.Key.KotlinBlog.toApollo())
        assertEquals(FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL, FeedSource.Key.KotlinYouTubeChannel.toApollo())
        assertEquals(FeedSourceKey.TALKING_KOTLIN_PODCAST, FeedSource.Key.TalkingKotlinPodcast.toApollo())
        assertEquals(FeedSourceKey.KOTLIN_WEEKLY, FeedSource.Key.KotlinWeekly.toApollo())
    }

    @Test
    fun `FeedSourcesQuery_FeedSource maps to expected FeedSource`() {
        FeedSource.Key.values().forEach { key ->
            val apolloKey = key.toApollo()
            val apolloFeedSource = FeedSourcesQuery.FeedSource(
                key = apolloKey,
                title = "Title for $key",
                description = "Description for $key",
            )

            val expectedFeedSource = FeedSource(
                key = key,
                title = "Title for $key",
                description = "Description for $key",
            )

            assertEquals(expectedFeedSource, apolloFeedSource.toModel())
        }
    }

    @Test
    fun `throws exception when mapping unknown FeedSourcesQuery_FeedSource to FeedSource`() {
        val apolloFeedSource = FeedSourcesQuery.FeedSource(
            key = FeedSourceKey.UNKNOWN__,
            title = "Kotlin Blog",
            description = "The official Kotlin blog",
        )

        assertFailsWith<IllegalStateException> {
            apolloFeedSource.toModel()
        }
    }
}
