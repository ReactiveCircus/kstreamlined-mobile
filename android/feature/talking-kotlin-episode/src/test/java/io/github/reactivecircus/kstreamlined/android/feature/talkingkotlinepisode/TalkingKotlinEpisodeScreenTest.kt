package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class TalkingKotlinEpisodeScreenTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TalkingKotlinEpisodeScreen_NotFound() {
        snapshotTester.snapshot {
            TalkingKotlinEpisodeScreenSnapshot(
                uiState = TalkingKotlinEpisodeUiState.NotFound,
            )
        }
    }

    @Test
    fun snapshot_TalkingKotlinEpisodeScreen_Content() {
        snapshotTester.snapshot {
            TalkingKotlinEpisodeScreenSnapshot(
                uiState = TalkingKotlinEpisodeUiState.Content(
                    episode = TalkingKotlinEpisode(
                        id = "1",
                        title = "Becoming a KMP Expert",
                        displayablePublishTime = "23 Jan 2025",
                        contentUrl = "content-url",
                        savedForLater = false,
                        audioUrl = "audio-url",
                        thumbnailUrl = "podcast-logo-url",
                        summary = "Sebastian and Pamela discuss what it means to be an expert in Kotlin Multiplatform, with the help of a panel of experts! Learn about why it's interesting to dive deep into a certain technology, recommendations on how to learn advanced topics, and general advice on how to get the most out of using KMP.",
                        duration = "52min.",
                        startPositionMillis = 0,
                    ),
                    isPlaying = false,
                ),
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun TalkingKotlinEpisodeScreenSnapshot(
        uiState: TalkingKotlinEpisodeUiState,
    ) {
        SharedTransitionLayout {
            TalkingKotlinEpisodeScreen(
                topBarBoundsKey = "",
                playerElementKey = "",
                onNavigateUp = {},
                onShareButtonClick = { _, _ -> },
                onOpenLink = {},
                uiState = uiState,
                eventSink = {},
            )
        }
    }
}
