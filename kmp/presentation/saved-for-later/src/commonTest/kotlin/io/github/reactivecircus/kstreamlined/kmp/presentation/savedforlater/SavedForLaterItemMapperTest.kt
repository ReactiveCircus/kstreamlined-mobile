package io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater

import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class SavedForLaterItemMapperTest {

    @Test
    fun `transformed Displayable FeedItems have expected displayable time`() {
        val timeZone = TimeZone.UTC

        val feedItems = listOf(
            FeedItem.KotlinBlog(
                id = "1",
                title = "Kotlin Blog 1",
                publishTime = Instant.parse("2023-12-03T03:10:00Z"),
                contentUrl = "content-url",
                savedForLater = false,
                featuredImageUrl = "feature-image-url",
            ),
            FeedItem.KotlinYouTube(
                id = "2",
                title = "Kotlin YouTube 1",
                publishTime = Instant.parse("2023-12-03T02:40:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                thumbnailUrl = "thumbnail-url",
                description = "description",
            ),
            FeedItem.KotlinWeekly(
                id = "3",
                title = "Kotlin Weekly #1",
                publishTime = Instant.parse("2023-12-02T22:10:54Z"),
                contentUrl = "content-url",
                savedForLater = false,
                issueNumber = 1,
            ),
        )

        val expectedDisplayableItems = listOf(
            DisplayableFeedItem(
                FeedItem.KotlinBlog(
                    id = "1",
                    title = "Kotlin Blog 1",
                    publishTime = Instant.parse("2023-12-03T03:10:00Z"),
                    contentUrl = "content-url",
                    savedForLater = false,
                    featuredImageUrl = "feature-image-url",
                ),
                displayablePublishTime = "03 Dec 2023",
            ),
            DisplayableFeedItem(
                FeedItem.KotlinYouTube(
                    id = "2",
                    title = "Kotlin YouTube 1",
                    publishTime = Instant.parse("2023-12-03T02:40:54Z"),
                    contentUrl = "content-url",
                    savedForLater = false,
                    thumbnailUrl = "thumbnail-url",
                    description = "description",
                ),
                displayablePublishTime = "03 Dec 2023",
            ),
            DisplayableFeedItem(
                FeedItem.KotlinWeekly(
                    id = "3",
                    title = "Kotlin Weekly #1",
                    publishTime = Instant.parse("2023-12-02T22:10:54Z"),
                    contentUrl = "content-url",
                    savedForLater = false,
                    issueNumber = 1,
                ),
                displayablePublishTime = "02 Dec 2023",
            ),
        )

        val actualDisplayableItems = feedItems.toSavedForLaterFeedItems(timeZone)

        assertEquals(expectedDisplayableItems, actualDisplayableItems)
    }
}
