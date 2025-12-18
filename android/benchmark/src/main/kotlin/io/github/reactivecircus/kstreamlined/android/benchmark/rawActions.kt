@file:Suppress("MagicNumber")

package io.github.reactivecircus.kstreamlined.android.benchmark

import android.graphics.Point
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiObject2

fun UiObject2.scrollDown() {
    setGestureMarginPercentage(0.2f)
    drag(Point(visibleCenter.x, -visibleCenter.y * 2))
}

fun UiObject2.flingDownUp() {
    setGestureMarginPercentage(0.2f)
    fling(Direction.DOWN)
    fling(Direction.UP)
}
