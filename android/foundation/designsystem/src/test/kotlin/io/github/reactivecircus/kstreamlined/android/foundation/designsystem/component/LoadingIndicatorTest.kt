package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

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
class LoadingIndicatorTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_LoadingIndicator() {
        snapshotTester.snapshot {
            LoadingIndicator(
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
