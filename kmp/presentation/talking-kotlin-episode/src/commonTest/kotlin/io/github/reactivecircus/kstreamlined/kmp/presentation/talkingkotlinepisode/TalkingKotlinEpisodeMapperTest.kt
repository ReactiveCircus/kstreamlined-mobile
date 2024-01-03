package io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class TalkingKotlinEpisodeMapperTest {

    @Test
    fun `transformed TalkingKotlinEpisode has expected displayable time`() {
        val timeZone = TimeZone.UTC

        val feedItem = FeedItem.TalkingKotlin(
            id = "1",
            title = "Talking Kotlin 1",
            publishTime = "2023-12-03T03:10:54Z".toInstant(),
            contentUrl = "content-url",
            savedForLater = false,
            audioUrl = "audio-url",
            thumbnailUrl = "podcast-logo-url",
            summary = "summary",
            duration = "35min.",
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
        )

        assertEquals(expectedTalkingKotlinEpisode, feedItem.asPresentationModel(timeZone))
    }
}
