package io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class KSIconsTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KSIcons() {
        snapshotTester.snapshot {
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 4,
            ) {
                KSIcons.asList().forEach {
                    Icon(
                        painter = it,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
