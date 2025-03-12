package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

class SeekBarTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SeekBar() {
        snapshotTester.snapshot(addSurface = false) {
            Surface(
                color = KSTheme.colorScheme.tertiary
            ) {
                SeekBar(
                    modifier = Modifier.padding(8.dp),
                    positionMillis = 1200_000,
                    durationMillis = 3000_000,
                    onPositionChangeFinished = {},
                )
            }
        }
    }
}
