package io.github.reactivecircus.kstreamlined.android.benchmark.feedselection

import androidx.benchmark.macro.MacrobenchmarkScope
import io.github.reactivecircus.kstreamlined.android.benchmark.FeedOriginKey

fun MacrobenchmarkScope.waitForFeedSelectionContent() {
    onElement { viewIdResourceName == "feedSelection:originList" }
}

fun MacrobenchmarkScope.toggleFeedOriginCard(key: FeedOriginKey) {
    onElement { viewIdResourceName == "feedOriginCard:${key.name}" }.click()
}
