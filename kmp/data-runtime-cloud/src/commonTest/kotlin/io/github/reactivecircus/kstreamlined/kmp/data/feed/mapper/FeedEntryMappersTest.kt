package io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmp.data.feed.model.FeedEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedEntryMappersTest {

    @Test
    fun `FeedEntriesQuery_KotlinBlogFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.KotlinBlogFeedEntry(
            id = "1",
            title = "Kotlin Blog Post",
            publishTimestamp = "2022-01-01T00:00:00Z",
            contentUrl = "https://blog.kotlinlang.org/post",
            featuredImageUrl = "https://blog.kotlinlang.org/image",
            description = "A blog post about Kotlin",
            __typename = "KotlinBlog"
        )

        val expectedFeedEntry = FeedEntry.KotlinBlog(
            id = "1",
            title = "Kotlin Blog Post",
            publishTimestamp = "2022-01-01T00:00:00Z",
            contentUrl = "https://blog.kotlinlang.org/post",
            featuredImageUrl = "https://blog.kotlinlang.org/image",
            description = "A blog post about Kotlin",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.toModel())
    }

    @Test
    fun `FeedEntriesQuery_KotlinYouTubeFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.KotlinYouTubeFeedEntry(
            id = "2",
            title = "Kotlin YouTube Video",
            publishTimestamp = "2022-01-02T00:00:00Z",
            contentUrl = "https://youtube.com/kotlinvideo",
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
            __typename = "KotlinYouTube",
        )

        val expectedFeedEntry = FeedEntry.KotlinYouTube(
            id = "2",
            title = "Kotlin YouTube Video",
            publishTimestamp = "2022-01-02T00:00:00Z",
            contentUrl = "https://youtube.com/kotlinvideo",
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.toModel())
    }

    @Test
    fun `FeedEntriesQuery_TalkingKotlinFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.TalkingKotlinFeedEntry(
            id = "3",
            title = "Talking Kotlin Podcast",
            publishTimestamp = "2022-01-03T00:00:00Z",
            contentUrl = "https://talkingkotlin.com/podcast",
            podcastLogoUrl = "https://talkingkotlin.com/podcast/logo",
            tags = listOf("Kotlin", "Podcast"),
            __typename = "TalkingKotlin",
        )

        val expectedFeedEntry = FeedEntry.TalkingKotlin(
            id = "3",
            title = "Talking Kotlin Podcast",
            publishTimestamp = "2022-01-03T00:00:00Z",
            contentUrl = "https://talkingkotlin.com/podcast",
            podcastLogoUrl = "https://talkingkotlin.com/podcast/logo",
            tags = listOf("Kotlin", "Podcast"),
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.toModel())
    }

    @Test
    fun `FeedEntriesQuery_KotlinWeeklyFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.KotlinWeeklyFeedEntry(
            id = "4",
            title = "Kotlin Weekly Newsletter",
            publishTimestamp = "2022-01-04T00:00:00Z",
            contentUrl = "https://kotlinweekly.net/newsletter",
            newsletterLogoUrl = "https://kotlinweekly.net/newsletter/logo",
            __typename = "KotlinWeekly",
        )

        val expectedFeedEntry = FeedEntry.KotlinWeekly(
            id = "4",
            title = "Kotlin Weekly Newsletter",
            publishTimestamp = "2022-01-04T00:00:00Z",
            contentUrl = "https://kotlinweekly.net/newsletter",
            newsletterLogoUrl = "https://kotlinweekly.net/newsletter/logo",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.toModel())
    }
}
