@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.onElement
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

fun MacrobenchmarkScope.scrollToCard(type: CardType) {
    val feedList = onElement { viewIdResourceName == "home:feedList" }
    feedList.setGestureMarginPercentage(0.2f)

    var scrollCount = 0
    while (onElementOrNull(500) { viewIdResourceName == type.resourceName } == null) {
        if (scrollCount > 5) {
            feedList.fling(Direction.DOWN)
            scrollCount = 0
        } else {
            feedList.scroll(Direction.DOWN, 1f, 10000)
            scrollCount++
        }
    }
    val card = onElement { viewIdResourceName == type.resourceName }
    if (feedList.visibleBounds.bottom - card.visibleBounds.bottom < 250) {
        feedList.scroll(Direction.DOWN, 0.5f, 10000)
    }
}

fun MacrobenchmarkScope.clickCard(type: CardType) {
    onElement { viewIdResourceName == type.resourceName }.click()
}

fun MacrobenchmarkScope.clickSaveButtonOnCard() {
    onElement { viewIdResourceName == "home:feedList" }
        .onElement { viewIdResourceName == "saveButton" }
        .click()
}

enum class CardType {
    KotlinBlog,
    KotlinYouTube,
    TalkingKotlin,
    KotlinWeekly,
    ;

    val resourceName: String
        get() = "${name.replaceFirstChar { it.lowercase(Locale.getDefault()) }}Card"
}
