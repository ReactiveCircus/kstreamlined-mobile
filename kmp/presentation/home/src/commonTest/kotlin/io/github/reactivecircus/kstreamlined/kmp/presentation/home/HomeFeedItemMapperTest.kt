package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class HomeFeedItemMapperTest {
    @Test
    fun `transformed HomeFeedItems are grouped by weeks with expected displayable time`() {
        val fixedClock = object : Clock {
            override fun now(): Instant = Instant.parse("2023-12-03T03:10:54Z")
        }
        val timeZone = TimeZone.UTC

        val feedItems = listOf(
            // this week
            // moments ago
            FeedItem.KotlinBlog(
                id = "1",
                title = "Kotlin Blog 1",
                publishTime = Instant.parse("2023-12-03T03:10:00Z"),
                contentUrl = "content-url",
                savedForLater = false,
                featuredImageUrl = "feature-image-url",
            ),
            // 30 minutes ago
            FeedItem.KotlinYouTube(
                id = "2",
                title = "Kotlin YouTube 1",
                publishTime = Instant.parse("2023-12-03T02:40:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                thumbnailUrl = "thumbnail-url",
                description = "description",
            ),
            // 5 hours ago
            FeedItem.KotlinWeekly(
                id = "3",
                title = "Kotlin Weekly #1",
                publishTime = Instant.parse("2023-12-02T22:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                issueNumber = 1,
            ),
            // yesterday
            FeedItem.TalkingKotlin(
                id = "4",
                title = "Talking Kotlin 1",
                publishTime = Instant.parse("2023-12-02T03:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                audioUrl = "audio-url",
                thumbnailUrl = "podcast-logo-url",
                summary = "summary",
                summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
                summaryPlainText = null,
                duration = "35min.",
                startPositionMillis = 0,
            ),
            // 5 days ago
            FeedItem.KotlinBlog(
                id = "5",
                title = "Kotlin Blog 2",
                publishTime = Instant.parse("2023-11-28T03:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                featuredImageUrl = "feature-image-url",
            ),
            // last week
            // 12 days ago
            FeedItem.KotlinYouTube(
                id = "6",
                title = "Kotlin YouTube 2",
                publishTime = Instant.parse("2023-11-21T03:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                thumbnailUrl = "thumbnail-url",
                description = "description",
            ),
            // earlier
            // 20 days ago
            FeedItem.KotlinWeekly(
                id = "7",
                title = "Kotlin Weekly #2",
                publishTime = Instant.parse("2023-11-13T03:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                issueNumber = 2,
            ),
        )

        val expectedHomeFeedItems = listOf(
            HomeFeedItem.SectionHeader("This week"),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinBlog(
                        id = "1",
                        title = "Kotlin Blog 1",
                        publishTime = Instant.parse("2023-12-03T03:10:00Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        featuredImageUrl = "feature-image-url",
                    ),
                    displayablePublishTime = "Moments ago",
                ),
            ),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinYouTube(
                        id = "2",
                        title = "Kotlin YouTube 1",
                        publishTime = Instant.parse("2023-12-03T02:40:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        thumbnailUrl = "thumbnail-url",
                        description = "description",
                    ),
                    displayablePublishTime = "30 minutes ago",
                ),
            ),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinWeekly(
                        id = "3",
                        title = "Kotlin Weekly #1",
                        publishTime = Instant.parse("2023-12-02T22:10:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        issueNumber = 1,
                    ),
                    displayablePublishTime = "5 hours ago",
                ),
            ),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.TalkingKotlin(
                        id = "4",
                        title = "Talking Kotlin 1",
                        publishTime = Instant.parse("2023-12-02T03:10:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        audioUrl = "audio-url",
                        thumbnailUrl = "podcast-logo-url",
                        summary = "summary",
                        duration = "35min.",
                        summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
                        summaryPlainText = null,
                        startPositionMillis = 0,
                    ),
                    displayablePublishTime = "Yesterday",
                ),
            ),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinBlog(
                        id = "5",
                        title = "Kotlin Blog 2",
                        publishTime = Instant.parse("2023-11-28T03:10:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        featuredImageUrl = "feature-image-url",
                    ),
                    displayablePublishTime = "5 days ago",
                ),
            ),
            HomeFeedItem.SectionHeader("Last week"),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinYouTube(
                        id = "6",
                        title = "Kotlin YouTube 2",
                        publishTime = Instant.parse("2023-11-21T03:10:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        thumbnailUrl = "thumbnail-url",
                        description = "description",
                    ),
                    displayablePublishTime = "21 Nov 2023",
                ),
            ),
            HomeFeedItem.SectionHeader("Earlier"),
            HomeFeedItem.Item(
                displayableFeedItem = DisplayableFeedItem(
                    FeedItem.KotlinWeekly(
                        id = "7",
                        title = "Kotlin Weekly #2",
                        publishTime = Instant.parse("2023-11-13T03:10:54Z"),
                        contentUrl = "content-url",
                        savedForLater = false,
                        issueNumber = 2,
                    ),
                    displayablePublishTime = "13 Nov 2023",
                ),
            ),
        )

        val actualHomeFeedItems = feedItems.toHomeFeedItems(fixedClock, timeZone)

        assertEquals(expectedHomeFeedItems, actualHomeFeedItems)
    }
}
