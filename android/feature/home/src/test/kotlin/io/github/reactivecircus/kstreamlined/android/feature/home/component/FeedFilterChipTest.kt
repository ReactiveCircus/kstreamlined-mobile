package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class FeedFilterChipTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_FeedFilterChip() {
        snapshotTester.snapshot {
            FeedFilterChip(
                showSkeleton = false,
                selectedFeedCount = 4,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Test
    fun snapshot_FeedFilterChip_skeleton() {
        snapshotTester.snapshot {
            FeedFilterChip(
                showSkeleton = true,
                selectedFeedCount = 0,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
