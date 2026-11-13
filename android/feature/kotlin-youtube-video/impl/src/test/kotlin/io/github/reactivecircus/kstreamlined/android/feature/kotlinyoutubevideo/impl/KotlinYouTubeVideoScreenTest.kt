package io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.animation.NoOpSharedElementLayout
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.core.web.WebViewFullScreenState
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideo
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideoUiState
import org.junit.Rule
import org.junit.Test

@Chameleon
class KotlinYouTubeVideoScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinYouTubeVideoScreen_NotFound() {
        snapshotTester.snapshot {
            KotlinYouTubeVideoScreenSnapshot(
                uiState = KotlinYouTubeVideoUiState.NotFound,
            )
        }
    }

    @Test
    fun snapshot_KotlinYouTubeVideoScreen_Content() {
        snapshotTester.snapshot {
            KotlinYouTubeVideoScreenSnapshot(
                uiState = KotlinYouTubeVideoUiState.Content(video = video),
            )
        }
    }

    @Test
    fun snapshot_PlayerErrorUi() {
        snapshotTester.snapshot {
            PlayerErrorUi(
                thumbnailUrl = video.thumbnailUrl,
                message = stringResource(R.string.video_unavailable_message),
                actionLabel = stringResource(R.string.watch_on_youtube),
                onAction = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
        }
    }

    @Composable
    fun KotlinYouTubeVideoScreenSnapshot(uiState: KotlinYouTubeVideoUiState) {
        NoOpSharedElementLayout {
            KotlinYouTubeVideoScreen(
                topBarBoundsKey = "",
                thumbnailBoundsKey = "",
                saveButtonBoundsKey = "",
                isFullScreenLayout = false,
                playerFullScreenState = WebViewFullScreenState(),
                isPlayerEnabled = true,
                onNavigateUp = {},
                onShareButtonClick = { _, _ -> },
                onWatchOnYouTube = {},
                uiState = uiState,
                eventSink = {},
            )
        }
    }

    private val video = KotlinYouTubeVideo(
        id = "yt:video:mQqwAmQBkss",
        title = "Simplify Spring WebFlux with Kotlin Coroutines",
        displayablePublishTime = "10 Nov 2026",
        contentUrl = "https://www.youtube.com/watch?v=mQqwAmQBkss",
        savedForLater = false,
        thumbnailUrl = "thumbnail-url",
        description = "Spring WebFlux is great, but reactive code can be hard to read. " +
            "Learn how Kotlin coroutines make it simpler: https://kotl.in/spring",
        videoId = "mQqwAmQBkss",
    )
}
