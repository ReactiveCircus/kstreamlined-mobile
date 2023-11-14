package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FeedOriginMappersTest {

    @Test
    fun `FeedOrigin_Key maps to expected FeedSourceKey`() {
        assertEquals(FeedSourceKey.KOTLIN_BLOG, FeedOrigin.Key.KotlinBlog.asApolloModel())
        assertEquals(FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL, FeedOrigin.Key.KotlinYouTubeChannel.asApolloModel())
        assertEquals(FeedSourceKey.TALKING_KOTLIN_PODCAST, FeedOrigin.Key.TalkingKotlinPodcast.asApolloModel())
        assertEquals(FeedSourceKey.KOTLIN_WEEKLY, FeedOrigin.Key.KotlinWeekly.asApolloModel())
    }

    @Test
    fun `FeedSourcesQuery_FeedSource maps to expected FeedOrigin`() {
        FeedOrigin.Key.entries.forEach { key ->
            val apolloKey = key.asApolloModel()
            val apolloFeedSource = FeedSourcesQuery.FeedSource(
                key = apolloKey,
                title = "Title for $key",
                description = "Description for $key",
            )

            val expectedFeedOrigin = FeedOrigin(
                key = key,
                title = "Title for $key",
                description = "Description for $key",
            )

            assertEquals(expectedFeedOrigin, apolloFeedSource.asExternalModel())
        }
    }

    @Test
    fun `throws exception when mapping unknown FeedSourcesQuery_FeedSource to FeedOrigin`() {
        val apolloFeedSource = FeedSourcesQuery.FeedSource(
            key = FeedSourceKey.UNKNOWN__,
            title = "Kotlin Blog",
            description = "The official Kotlin blog",
        )

        assertFailsWith<IllegalStateException> {
            apolloFeedSource.asExternalModel()
        }
    }
}
