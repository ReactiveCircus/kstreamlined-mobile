package io.github.reactivecircus.kstreamlined.android.benchmark.tldrsummary

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForTldrSummaryContent() {
    onElement { viewIdResourceName == "tldrSummary:content" }
}
