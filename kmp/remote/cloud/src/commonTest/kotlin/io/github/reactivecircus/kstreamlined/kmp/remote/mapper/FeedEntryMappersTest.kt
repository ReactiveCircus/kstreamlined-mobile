package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinWeekly
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.graphql.type.buildTalkingKotlin
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class FeedEntryMappersTest {

    @Test
    fun `KotlinBlogFeedEntryItem maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinBlog {
                    id = "1"
                    title = "Kotlin Blog Post"
                    publishTime = Instant.parse("2022-01-01T00:00:00Z")
                    contentUrl = "https://blog.kotlinlang.org/post"
                    featuredImageUrl = "https://blog.kotlinlang.org/image"
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinBlog(
            id = "1",
            title = "Kotlin Blog Post",
            publishTime = Instant.parse("2022-01-01T00:00:00Z"),
            contentUrl = "https://blog.kotlinlang.org/post",
            featuredImageUrl = "https://blog.kotlinlang.org/image",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.feedEntryItem.asExternalModel())
    }

    @Test
    fun `KotlinYouTubeFeedEntryItem maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinYouTube {
                    id = "2"
                    title = "Kotlin YouTube Video"
                    publishTime = Instant.parse("2022-01-02T00:00:00Z")
                    contentUrl = "https://youtube.com/kotlinvideo"
                    thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail"
                    description = "A YouTube video about Kotlin"
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinYouTube(
            id = "2",
            title = "Kotlin YouTube Video",
            publishTime = Instant.parse("2022-01-02T00:00:00Z"),
            contentUrl = "https://youtube.com/kotlinvideo",
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.feedEntryItem.asExternalModel())
    }

    @Test
    fun `TalkingKotlinFeedEntryItem maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildTalkingKotlin {
                    id = "3"
                    title = "Talking Kotlin Podcast"
                    publishTime = Instant.parse("2022-01-03T00:00:00Z")
                    contentUrl = "https://talkingkotlin.com/podcast"
                    audioUrl = "audio.mp3"
                    thumbnailUrl = "https://talkingkotlin.com/podcast/logo"
                    summary = "Talking kotlin podcast episode summary"
                    duration = "35min."
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.TalkingKotlin(
            id = "3",
            title = "Talking Kotlin Podcast",
            publishTime = Instant.parse("2022-01-03T00:00:00Z"),
            contentUrl = "https://talkingkotlin.com/podcast",
            audioUrl = "audio.mp3",
            thumbnailUrl = "https://talkingkotlin.com/podcast/logo",
            summary = "Talking kotlin podcast episode summary",
            duration = "35min.",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.feedEntryItem.asExternalModel())
    }

    @Test
    fun `KotlinWeeklyFeedEntryItem maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinWeekly {
                    id = "4"
                    title = "Kotlin Weekly #381"
                    publishTime = Instant.parse("2022-01-04T00:00:00Z")
                    contentUrl = "https://kotlinweekly.net/newsletter"
                    issueNumber = 381
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinWeekly(
            id = "4",
            title = "Kotlin Weekly #381",
            publishTime = Instant.parse("2022-01-04T00:00:00Z"),
            contentUrl = "https://kotlinweekly.net/newsletter",
            issueNumber = 381,
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.feedEntryItem.asExternalModel())
    }
}
