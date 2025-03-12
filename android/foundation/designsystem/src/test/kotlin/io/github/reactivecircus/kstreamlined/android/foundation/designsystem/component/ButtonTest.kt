package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

class ButtonTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Button() {
        snapshotTester.snapshot {
            Button(
                text = "Button",
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
