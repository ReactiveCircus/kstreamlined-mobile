package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.foundation.ScrollState
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class TldrSummaryScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TldrSummaryScreen() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                title = SampleTitle,
                content = rememberSampleTldrContent(),
                displayableGeneratedTime = SampleGeneratedTime,
                model = SampleModel,
                onNavigateUp = {},
                onReadOriginal = {},
            )
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_ScrolledToBottom() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                title = SampleTitle,
                content = rememberSampleTldrContent(),
                displayableGeneratedTime = SampleGeneratedTime,
                model = SampleModel,
                onNavigateUp = {},
                onReadOriginal = {},
                scrollState = ScrollState(Int.MAX_VALUE),
            )
        }
    }
}
