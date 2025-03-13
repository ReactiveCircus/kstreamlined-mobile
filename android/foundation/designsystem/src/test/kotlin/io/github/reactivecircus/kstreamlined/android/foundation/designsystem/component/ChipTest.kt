package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class ChipTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Chip() {
        snapshotTester.snapshot {
            Chip(
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(
                    text = "Chip",
                    style = KSTheme.typography.labelLarge,
                )
                Icon(KSIcons.ArrowDown, contentDescription = null)
            }
        }
    }
}
