package io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.about

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
class VersionTileTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_VersionTile() {
        snapshotTester.snapshot {
            VersionTile(
                version = "android-0.3.0 (4c52de9)",
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
