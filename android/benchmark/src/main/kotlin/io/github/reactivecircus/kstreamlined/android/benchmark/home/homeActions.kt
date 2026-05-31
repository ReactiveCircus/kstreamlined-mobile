@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.onElement
import io.github.reactivecircus.kstreamlined.android.benchmark.FeedOriginKey
import io.github.reactivecircus.kstreamlined.android.benchmark.flingDownUp
import io.github.reactivecircus.kstreamlined.android.benchmark.scrollDown
import java.util.Locale

fun MacrobenchmarkScope.waitForHomeFeedContent() {
    onElement { viewIdResourceName == "home:feedList" }
}

fun MacrobenchmarkScope.homeFeedListScrollDown() {
    onElement { viewIdResourceName == "home:feedList" }.scrollDown()
}

fun MacrobenchmarkScope.homeFeedListFlingDownUp() {
    onElement { viewIdResourceName == "home:feedList" }.flingDownUp()
}

fun MacrobenchmarkScope.clickCard(type: CardType) {
    onElement { viewIdResourceName == type.resourceName }.click()
}

fun MacrobenchmarkScope.clickSaveButtonOnCard() {
    onElement { viewIdResourceName == "home:feedList" }
        .onElement { viewIdResourceName == "saveButton" }
        .click()
}

fun MacrobenchmarkScope.clickFilterChip() {
    onElement { viewIdResourceName == "home:feedFilterChip" }.click()
}

enum class CardType {
    KotlinBlog,
    KotlinYouTube,
    TalkingKotlin,
    KotlinWeekly,
    ;

    val resourceName: String
        get() = "${name.replaceFirstChar { it.lowercase(Locale.getDefault()) }}Card"

    fun toFeedOriginKey(): FeedOriginKey = when (this) {
        KotlinBlog -> FeedOriginKey.KotlinBlog
        KotlinYouTube -> FeedOriginKey.KotlinYouTubeChannel
        TalkingKotlin -> FeedOriginKey.TalkingKotlinPodcast
        KotlinWeekly -> FeedOriginKey.KotlinWeekly
    }
}
