package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class ButtonTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Button() {
        snapshotTester.snapshot {
            val config = Config(
                content = {},
            )
            config.content // this throws NoSuchMethodError when `Config` comes from a separate compilation.

            Button(
                text = "Button",
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
