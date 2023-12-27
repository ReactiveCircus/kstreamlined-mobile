package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinWeekly
import io.github.reactivecircus.kstreamlined.graphql.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.graphql.type.buildTalkingKotlin
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedEntryMappersTest {

    @Test
    fun `FeedEntriesQuery_KotlinBlogFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinBlog {
                    id = "1"
                    title = "Kotlin Blog Post"
                    publishTime = "2022-01-01T00:00:00Z".toInstant()
                    contentUrl = "https://blog.kotlinlang.org/post"
                    featuredImageUrl = "https://blog.kotlinlang.org/image"
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinBlog(
            id = "1",
            title = "Kotlin Blog Post",
            publishTime = "2022-01-01T00:00:00Z".toInstant(),
            contentUrl = "https://blog.kotlinlang.org/post",
            featuredImageUrl = "https://blog.kotlinlang.org/image",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.asExternalModel())
    }

    @Test
    fun `FeedEntriesQuery_KotlinYouTubeFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinYouTube {
                    id = "2"
                    title = "Kotlin YouTube Video"
                    publishTime = "2022-01-02T00:00:00Z".toInstant()
                    contentUrl = "https://youtube.com/kotlinvideo"
                    thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail"
                    description = "A YouTube video about Kotlin"
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinYouTube(
            id = "2",
            title = "Kotlin YouTube Video",
            publishTime = "2022-01-02T00:00:00Z".toInstant(),
            contentUrl = "https://youtube.com/kotlinvideo",
            thumbnailUrl = "https://youtube.com/kotlinvideo/thumbnail",
            description = "A YouTube video about Kotlin",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.asExternalModel())
    }

    @Test
    fun `FeedEntriesQuery_TalkingKotlinFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildTalkingKotlin {
                    id = "3"
                    title = "Talking Kotlin Podcast"
                    publishTime = "2022-01-03T00:00:00Z".toInstant()
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
            publishTime = "2022-01-03T00:00:00Z".toInstant(),
            contentUrl = "https://talkingkotlin.com/podcast",
            audioUrl = "audio.mp3",
            thumbnailUrl = "https://talkingkotlin.com/podcast/logo",
            summary = "Talking kotlin podcast episode summary",
            duration = "35min.",
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.asExternalModel())
    }

    @Test
    fun `FeedEntriesQuery_KotlinWeeklyFeedEntry maps to expected FeedEntry`() {
        val apolloFeedEntry = FeedEntriesQuery.Data {
            feedEntries = listOf(
                buildKotlinWeekly {
                    id = "4"
                    title = "Kotlin Weekly #381"
                    publishTime = "2022-01-04T00:00:00Z".toInstant()
                    contentUrl = "https://kotlinweekly.net/newsletter"
                    issueNumber = 381
                },
            )
        }.feedEntries.first()

        val expectedFeedEntry = FeedEntry.KotlinWeekly(
            id = "4",
            title = "Kotlin Weekly #381",
            publishTime = "2022-01-04T00:00:00Z".toInstant(),
            contentUrl = "https://kotlinweekly.net/newsletter",
            issueNumber = 381,
        )

        assertEquals(expectedFeedEntry, apolloFeedEntry.asExternalModel())
    }
}
