package io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo

import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class KotlinYouTubeVideoMapperTest {
    private val timeZone = TimeZone.UTC

    private val feedItem = FeedItem.KotlinYouTube(
        id = "yt:video:mQqwAmQBkss",
        title = "Kotlin YouTube Video",
        publishTime = Instant.parse("2023-12-03T03:10:54Z"),
        contentUrl = "https://www.youtube.com/watch?v=mQqwAmQBkss",
        savedForLater = false,
        thumbnailUrl = "thumbnail-url",
        description = "description",
        videoId = "mQqwAmQBkss",
    )

    @Test
    fun `transformed KotlinYouTubeVideo has expected values`() {
        val expectedVideo = KotlinYouTubeVideo(
            id = "yt:video:mQqwAmQBkss",
            title = "Kotlin YouTube Video",
            displayablePublishTime = "3 Dec 2023",
            contentUrl = "https://www.youtube.com/watch?v=mQqwAmQBkss",
            savedForLater = false,
            thumbnailUrl = "thumbnail-url",
            description = "description",
            videoId = "mQqwAmQBkss",
        )

        assertEquals(expectedVideo, feedItem.asPresentationModel(timeZone = timeZone))
    }
}
