package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

class IconButtonTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_LargeIconButton() {
        snapshotTester.snapshot {
            LargeIconButton(
                KSIcons.Close,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Test
    fun snapshot_FilledIconButton() {
        snapshotTester.snapshot {
            FilledIconButton(
                KSIcons.Settings,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
