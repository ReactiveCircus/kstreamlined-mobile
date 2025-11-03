package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class PodcastPlayerUiTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_PodcastPlayerUi_paused() {
        snapshotTester.snapshot {
            PodcastPlayerUi(
                playerPositionMillis = 1200_000,
                playerDurationMillis = 3000_000,
                onPositionChange = {},
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    summaryIsHtml = false,
                    duration = "35min.",
                    startPositionMillis = 0,
                ),
                isPlaying = false,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Test
    fun snapshot_PodcastPlayerUi_playing() {
        snapshotTester.snapshot {
            PodcastPlayerUi(
                playerPositionMillis = 1200_000,
                playerDurationMillis = 3000_000,
                onPositionChange = {},
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    summaryIsHtml = false,
                    duration = "35min.",
                    startPositionMillis = 0,
                ),
                isPlaying = true,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
