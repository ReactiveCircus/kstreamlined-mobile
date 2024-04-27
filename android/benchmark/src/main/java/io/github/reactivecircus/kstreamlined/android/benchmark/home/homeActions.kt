@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import io.github.reactivecircus.kstreamlined.android.benchmark.flingDownUp
import io.github.reactivecircus.kstreamlined.android.benchmark.scrollDown
import io.github.reactivecircus.kstreamlined.android.benchmark.waitAndFindObject
import java.util.Locale

fun MacrobenchmarkScope.waitForHomeFeedContent() {
    device.waitAndFindObject(By.res("home:feedList"), 10_000)
}

fun MacrobenchmarkScope.homeFeedListScrollDown() {
    device.scrollDown(device.findObject(By.res("home:feedList")))
}

fun MacrobenchmarkScope.homeFeedListFlingDownUp() {
    device.flingDownUp(device.findObject(By.res("home:feedList")))
}

fun MacrobenchmarkScope.scrollToCard(type: CardType) {
    val feedList = device.findObject(By.res("home:feedList"))
    feedList.setGestureMarginPercentage(0.2f)
    if (!device.hasObject(By.res(type.resourceName))) {
        feedList.fling(Direction.DOWN)
        var foundCard = false
        while (!foundCard) {
            if (device.hasObject(By.res(type.resourceName))) {
                foundCard = true
            } else {
                feedList.scroll(Direction.DOWN, 1f, 10000)
            }
        }
    }
    val card = device.findObject(By.res(type.resourceName))
    if (feedList.visibleBounds.bottom - card.visibleBounds.bottom < 250) {
        feedList.scroll(Direction.DOWN, 0.5f, 10000)
    }
}

fun MacrobenchmarkScope.clickCard(type: CardType) {
    device.findObject(By.res(type.resourceName)).click()
    device.waitForIdle()
}

fun MacrobenchmarkScope.clickSaveButtonOnCard() {
    val feedList = device.findObject(By.res("home:feedList"))
    feedList.findObject(By.res("saveButton")).click()
    device.waitForIdle()
}

enum class CardType {
    KotlinBlog,
    KotlinYouTube,
    TalkingKotlin,
    KotlinWeekly;

    val resourceName: String
        get() = "${name.replaceFirstChar { it.lowercase(Locale.getDefault()) }}Card"
}
