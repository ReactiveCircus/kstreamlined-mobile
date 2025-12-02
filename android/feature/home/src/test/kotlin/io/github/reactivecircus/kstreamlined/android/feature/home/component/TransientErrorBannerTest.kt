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
class TransientErrorBannerTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TransientErrorBanner() {
        snapshotTester.snapshot {
            TransientErrorBanner(
                onDismiss = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
