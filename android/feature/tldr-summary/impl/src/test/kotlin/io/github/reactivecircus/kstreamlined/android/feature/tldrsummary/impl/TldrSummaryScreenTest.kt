package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.foundation.ScrollState
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryUiState
import org.junit.Rule
import org.junit.Test

@Chameleon
class TldrSummaryScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TldrSummaryScreen_Loading() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                title = SampleTitle,
                uiState = TldrSummaryUiState.Loading,
                onNavigateUp = {},
                onReadOriginal = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Error() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                title = SampleTitle,
                uiState = TldrSummaryUiState.Error,
                onNavigateUp = {},
                onReadOriginal = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Content() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                title = SampleTitle,
                uiState = TldrSummaryUiState.Content(SampleTldrSummary),
                onNavigateUp = {},
                onReadOriginal = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Content_ScrolledToBottom() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
                tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                title = SampleTitle,
                uiState = TldrSummaryUiState.Content(SampleTldrSummary),
                onNavigateUp = {},
                onReadOriginal = {},
                onRetry = {},
                scrollState = ScrollState(Int.MAX_VALUE),
            )
        }
    }
}
