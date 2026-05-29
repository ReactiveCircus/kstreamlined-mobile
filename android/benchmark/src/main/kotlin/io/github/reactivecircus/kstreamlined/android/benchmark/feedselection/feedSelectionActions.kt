@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark.feedselection

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForFeedSelectionContent() {
    onElement { viewIdResourceName == "feedSelection:originList" }
}

fun MacrobenchmarkScope.toggleFeedOriginCard(key: FeedOriginKey) {
    onElement { viewIdResourceName == "feedOriginCard:${key.resourceName}" }.click()
}

enum class FeedOriginKey(val resourceName: String) {
    KotlinBlog("KotlinBlog"),
    KotlinYouTubeChannel("KotlinYouTubeChannel"),
    TalkingKotlinPodcast("TalkingKotlinPodcast"),
    KotlinWeekly("KotlinWeekly"),
}
