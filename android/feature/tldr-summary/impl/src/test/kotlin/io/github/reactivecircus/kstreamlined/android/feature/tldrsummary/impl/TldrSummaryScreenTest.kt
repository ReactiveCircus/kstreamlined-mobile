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
    fun snapshot_TldrSummaryScreen_Loading() {
        snapshotTester.snapshot {
            TldrSummaryScreen(
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
                title = SampleTitle,
                uiState = TldrSummaryUiState.Content(
                    summary = rememberSampleTldrSummary(),
                    model = SampleModel,
                    displayableGeneratedTime = SampleGeneratedTime,
                ),
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
                title = SampleTitle,
                uiState = TldrSummaryUiState.Content(
                    summary = rememberSampleTldrSummary(),
                    model = SampleModel,
                    displayableGeneratedTime = SampleGeneratedTime,
                ),
                onNavigateUp = {},
                onReadOriginal = {},
                onRetry = {},
                scrollState = ScrollState(Int.MAX_VALUE),
            )
        }
    }
}
