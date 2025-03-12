package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

class DividerTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_HorizontalDivider() {
        snapshotTester.snapshot {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "item 1", style = KSTheme.typography.titleMedium)
                HorizontalDivider(
                    modifier = Modifier.width(120.dp),
                )
                Text(text = "item 2", style = KSTheme.typography.titleMedium)
            }
        }
    }

    @Test
    fun snapshot_VerticalDivider() {
        snapshotTester.snapshot {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "item 1", style = KSTheme.typography.titleMedium)
                VerticalDivider(
                    modifier = Modifier.height(120.dp),
                )
                Text(text = "item 2", style = KSTheme.typography.titleMedium)
            }
        }
    }
}
