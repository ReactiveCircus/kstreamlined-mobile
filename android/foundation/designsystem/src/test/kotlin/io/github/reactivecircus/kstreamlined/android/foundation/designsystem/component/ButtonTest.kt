package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
@Chameleon
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
