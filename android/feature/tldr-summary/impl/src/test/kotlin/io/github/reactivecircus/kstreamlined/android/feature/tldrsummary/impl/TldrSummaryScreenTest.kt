package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.foundation.ScrollState
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.animation.NoOpSharedElementLayout
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
            NoOpSharedElementLayout {
                TldrSummaryScreen(
                    tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                    topBarBoundsKey = "",
                    tldrButtonBoundsKey = "",
                    titleBoundsKey = "",
                    title = SampleTitle,
                    onNavigateUp = {},
                    onReadOriginal = {},
                    uiState = TldrSummaryUiState.Loading,
                    eventSink = {},
                )
            }
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Error() {
        snapshotTester.snapshot {
            NoOpSharedElementLayout {
                TldrSummaryScreen(
                    tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                    topBarBoundsKey = "",
                    tldrButtonBoundsKey = "",
                    titleBoundsKey = "",
                    title = SampleTitle,
                    onNavigateUp = {},
                    onReadOriginal = {},
                    uiState = TldrSummaryUiState.Error,
                    eventSink = {},
                )
            }
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Content() {
        snapshotTester.snapshot {
            NoOpSharedElementLayout {
                TldrSummaryScreen(
                    tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                    topBarBoundsKey = "",
                    tldrButtonBoundsKey = "",
                    titleBoundsKey = "",
                    title = SampleTitle,
                    onNavigateUp = {},
                    onReadOriginal = {},
                    uiState = TldrSummaryUiState.Content(SampleTldrSummary),
                    eventSink = {},
                )
            }
        }
    }

    @Test
    fun snapshot_TldrSummaryScreen_Content_ScrolledToBottom() {
        snapshotTester.snapshot {
            NoOpSharedElementLayout {
                TldrSummaryScreen(
                    tldrSummaryRenderer = TldrSummaryRenderer.Sample,
                    topBarBoundsKey = "",
                    tldrButtonBoundsKey = "",
                    titleBoundsKey = "",
                    title = SampleTitle,
                    onNavigateUp = {},
                    onReadOriginal = {},
                    uiState = TldrSummaryUiState.Content(SampleTldrSummary),
                    eventSink = {},
                    scrollState = ScrollState(Int.MAX_VALUE),
                )
            }
        }
    }
}
