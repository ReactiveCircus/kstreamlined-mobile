package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class TldrButtonTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TldrButton() {
        snapshotTester.snapshot {
            TldrButton(
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
