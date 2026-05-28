package io.github.reactivecircus.android.feature.feedselection.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.ModalBottomSheet
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.rememberModalBottomSheetState
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.FeedOriginList
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.SampleFeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import org.junit.Rule
import org.junit.Test

@Chameleon
class FeedSelectionBottomSheetTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_FeedSelectionBottomSheet_allSelected() {
        snapshotTester.snapshot {
            FeedSelectionBottomSheetSnapshot(
                feedOrigins = listOf(
                    SampleFeedOrigin.KotlinBlog,
                    SampleFeedOrigin.KotlinYouTubeChannel,
                    SampleFeedOrigin.TalkingKotlinPodcast,
                    SampleFeedOrigin.KotlinWeekly,
                ),
            )
        }
    }

    @Test
    fun snapshot_FeedSelectionBottomSheet_someSelected() {
        snapshotTester.snapshot {
            FeedSelectionBottomSheetSnapshot(
                feedOrigins = listOf(
                    SampleFeedOrigin.KotlinBlog.copy(selected = false),
                    SampleFeedOrigin.KotlinYouTubeChannel,
                    SampleFeedOrigin.TalkingKotlinPodcast.copy(selected = false),
                    SampleFeedOrigin.KotlinWeekly,
                ),
            )
        }
    }

    @Composable
    fun FeedSelectionBottomSheetSnapshot(feedOrigins: List<FeedOrigin>) {
        Box(modifier = Modifier.fillMaxSize()) {
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = rememberModalBottomSheetState(
                    initiallyExpanded = true,
                    skipPartiallyExpanded = true,
                ),
            ) {
                FeedOriginList(
                    feedOrigins = feedOrigins,
                    onToggle = {},
                )
            }
        }
    }
}
