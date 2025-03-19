package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class SyncButtonTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SyncButton() {
        snapshotTester.snapshot {
            SyncButton(
                showSkeleton = false,
                syncing = true,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Test
    fun snapshot_SyncButton_skeleton() {
        snapshotTester.snapshot {
            SyncButton(
                showSkeleton = true,
                syncing = true,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
