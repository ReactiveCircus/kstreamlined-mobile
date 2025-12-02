package io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync

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
class AutoSyncSwitchTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_AutoSyncSwitch(
        selected: Boolean,
    ) {
        snapshotTester.snapshot {
            AutoSyncSwitch(
                selected = selected,
                onSelectedChange = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
