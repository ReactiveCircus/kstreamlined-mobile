package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class ProgressIndicatorTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_LinearProgressIndicator() {
        snapshotTester.snapshot {
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp),
            )
        }
    }
}
