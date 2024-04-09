@file:Suppress("MagicNumber")

package io.github.reactivecircus.benchmark.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import io.github.reactivecircus.benchmark.flingDownUp
import io.github.reactivecircus.benchmark.scrollDown
import io.github.reactivecircus.benchmark.waitAndFindObject

fun MacrobenchmarkScope.waitForHomeFeedContent() {
    device.waitAndFindObject(By.res("home:feedList"), 5_000)
}

fun MacrobenchmarkScope.homeFeedListScrollDown() {
    device.scrollDown(device.findObject(By.res("home:feedList")))
}

fun MacrobenchmarkScope.homeFeedListFlingDownUp() {
    device.flingDownUp(device.findObject(By.res("home:feedList")))
}

fun MacrobenchmarkScope.clickSaveButtonOnCard() {
    val feedList = device.findObject(By.res("home:feedList"))
    feedList.findObject(By.res("saveButton")).click()
    device.waitForIdle()
}
