package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
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
