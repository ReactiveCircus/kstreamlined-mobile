package io.github.reactivecircus.kstreamlined.android.core.ui.pattern

import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class ItemNotFoundUiTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_ItemNotFoundUi() {
        snapshotTester.snapshot {
            ItemNotFoundUi()
        }
    }
}
