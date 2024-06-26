package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class TalkingKotlinEpisodeMapperTest {

    @Test
    fun `transformed TalkingKotlinEpisode has expected displayable time`() {
        val timeZone = TimeZone.UTC

        val feedItem = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin 1",
            publishTime = Instant.parse("2023-12-03T03:10:54Z"),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio-url",
            thumbnailUrl = "podcast-logo-url",
            summary = "summary",
            duration = "35min.",
            startPositionMillis = 3000,
        )

        val expectedTalkingKotlinEpisode = TalkingKotlinEpisode(
            id = "1",
            title = "Talking Kotlin 1",
            displayablePublishTime = "03 Dec 2023",
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio-url",
            thumbnailUrl = "podcast-logo-url",
            summary = "summary",
            duration = "35min.",
            startPositionMillis = 3000,
        )

        assertEquals(expectedTalkingKotlinEpisode, feedItem.asPresentationModel(timeZone))
    }
}
