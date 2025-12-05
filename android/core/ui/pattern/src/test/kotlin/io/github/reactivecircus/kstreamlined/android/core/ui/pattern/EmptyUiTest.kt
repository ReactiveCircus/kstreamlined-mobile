package io.github.reactivecircus.kstreamlined.android.core.ui.pattern

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class EmptyUiTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_EmptyUi() {
        snapshotTester.snapshot {
            EmptyUi()
        }
    }
}
