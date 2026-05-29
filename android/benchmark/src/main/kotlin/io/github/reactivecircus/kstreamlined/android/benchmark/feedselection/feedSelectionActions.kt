@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark.feedselection

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction

fun MacrobenchmarkScope.waitForFeedSelectionContent() {
    onElement { viewIdResourceName == "feedSelection:originList" }
}

fun MacrobenchmarkScope.toggleFeedOriginCard(key: FeedOriginKey) {
    onElement { viewIdResourceName == "feedOriginCard:${key.resourceName}" }.click()
}

fun MacrobenchmarkScope.dismissFeedSelectionSheet() {
    val originList = onElement { viewIdResourceName == "feedSelection:originList" }
    originList.setGestureMarginPercentage(0.1f)
    originList.fling(Direction.DOWN)
}

enum class FeedOriginKey(val resourceName: String) {
    KotlinBlog("KotlinBlog"),
    KotlinYouTubeChannel("KotlinYouTubeChannel"),
    TalkingKotlinPodcast("TalkingKotlinPodcast"),
    KotlinWeekly("KotlinWeekly"),
}
