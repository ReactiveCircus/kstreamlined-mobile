package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisodeUiState
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
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
                        summary = "<p>Sebastian and Pamela discuss what it means to be an expert in Kotlin Multiplatform, with the help of a panel of experts! Learn about why it's interesting to dive deep into a certain technology, recommendations on how to learn advanced topics, and general advice on how to get the most out of using KMP.</p><p><a href=\"#test1\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><strong>SKIE</strong></a></p><p>KMP libraries:</p><ul> <li><a href=\"#test2\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>whyoleg/cryptography-kotlin</u></a></li></ul><p>Interesting code to explore:</p><ul> <li><a href=\"#test3\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>JetBrains/kotlin</u></a></li> <li><a href=\"#test4\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>Kotlin/kotlinx-datetime</u></a></li> <li><a href=\"#test5\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>Kotlin/kotlinx-io</u></a></li> <li><a href=\"#test6\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>Kotlin/kotlinx.coroutines</u></a></li> <li><a href=\"#test7\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>Cartesian product</u></a> by Jake </li></ul><ul> <li><a href=\"#test8\" target=\"_blank\" rel=\"ugc noopener noreferrer\"><u>Get an invite!</u></a></li></ul>",
                        summaryIsHtml = true,
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
    fun TalkingKotlinEpisodeScreenSnapshot(uiState: TalkingKotlinEpisodeUiState) {
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
