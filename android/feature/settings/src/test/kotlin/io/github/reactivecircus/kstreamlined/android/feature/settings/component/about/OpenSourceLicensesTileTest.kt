package io.github.reactivecircus.kstreamlined.android.feature.settings.component.about

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class OpenSourceLicensesTileTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_OpenSourceLicensesTile() {
        snapshotTester.snapshot {
            OpenSourceLicensesTile(
                onClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
