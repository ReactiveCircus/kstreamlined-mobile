package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.fragment.FeedSourceItem
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FeedSourceMappersTest {
    @Test
    fun `FeedSource_Key maps to expected FeedSourceKey`() {
        assertEquals(FeedSourceKey.KOTLIN_BLOG, FeedSource.Key.KotlinBlog.asApolloModel())
        assertEquals(FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL, FeedSource.Key.KotlinYouTubeChannel.asApolloModel())
        assertEquals(FeedSourceKey.TALKING_KOTLIN_PODCAST, FeedSource.Key.TalkingKotlinPodcast.asApolloModel())
        assertEquals(FeedSourceKey.KOTLIN_WEEKLY, FeedSource.Key.KotlinWeekly.asApolloModel())
    }

    @Test
    fun `FeedSourceItem maps to expected FeedSource`() {
        FeedSource.Key.entries.forEach { key ->
            val apolloKey = key.asApolloModel()
            val apolloFeedSource = FeedSourceItem(
                key = apolloKey,
                title = "Title for $key",
                description = "Description for $key",
            )

            val expectedFeedOrigin = FeedSource(
                key = key,
                title = "Title for $key",
                description = "Description for $key",
            )

            assertEquals(expectedFeedOrigin, apolloFeedSource.asExternalModel())
        }
    }

    @Test
    fun `returns null when mapping unknown FeedSourceItem to FeedSource`() {
        val apolloFeedSource = FeedSourceItem(
            key = FeedSourceKey.UNKNOWN__,
            title = "Unknown feed source title",
            description = "Unknown feed source description",
        )

        assertNull(apolloFeedSource.asExternalModel())
    }
}
