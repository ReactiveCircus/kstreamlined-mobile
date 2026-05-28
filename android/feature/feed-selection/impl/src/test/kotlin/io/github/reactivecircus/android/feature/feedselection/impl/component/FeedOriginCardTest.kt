package io.github.reactivecircus.android.feature.feedselection.impl.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.SampleFeedOrigin
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.component.FeedOriginCard
import org.junit.Rule
import org.junit.Test

@Chameleon
class FeedOriginCardTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_FeedOriginCard_KotlinBlog(selected: Boolean) {
        snapshotTester.snapshot {
            FeedOriginCard(
                origin = SampleFeedOrigin.KotlinBlog.copy(selected = selected),
                onToggle = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }

    @Test
    fun snapshot_FeedOriginCard_KotlinYouTubeChannel(selected: Boolean) {
        snapshotTester.snapshot {
            FeedOriginCard(
                origin = SampleFeedOrigin.KotlinYouTubeChannel.copy(selected = selected),
                onToggle = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }

    @Test
    fun snapshot_FeedOriginCard_TalkingKotlinPodcast(selected: Boolean) {
        snapshotTester.snapshot {
            FeedOriginCard(
                origin = SampleFeedOrigin.TalkingKotlinPodcast.copy(selected = selected),
                onToggle = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }

    @Test
    fun snapshot_FeedOriginCard_KotlinWeekly(selected: Boolean) {
        snapshotTester.snapshot {
            FeedOriginCard(
                origin = SampleFeedOrigin.KotlinWeekly.copy(selected = selected),
                onToggle = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
