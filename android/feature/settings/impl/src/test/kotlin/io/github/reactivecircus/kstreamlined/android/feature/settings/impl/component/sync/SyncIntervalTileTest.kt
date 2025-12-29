package io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.sync

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.AutoSyncInterval
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class SyncIntervalTileTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SyncIntervalTile(
        syncInterval: AutoSyncInterval,
    ) {
        snapshotTester.snapshot {
            SyncIntervalTile(
                selectedSyncInterval = syncInterval,
                onClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
