package io.github.reactivecircus.kstreamlined.android.benchmark.contentviewer

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForContentViewerContent() {
    onElement { viewIdResourceName == "contentViewer:content" }
}
