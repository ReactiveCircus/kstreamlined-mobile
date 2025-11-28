package io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.AutoSyncInterval
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class SyncIntervalPickerTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SyncIntervalPicker(
        selectedSyncInterval: AutoSyncInterval,
    ) {
        snapshotTester.snapshot {
            SyncIntervalPicker(
                selectedSyncInterval = selectedSyncInterval,
                onSelectSyncInterval = {},
            )
        }
    }
}
