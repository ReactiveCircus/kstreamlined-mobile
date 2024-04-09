@file:Suppress("MagicNumber")

package io.github.reactivecircus.benchmark

import android.graphics.Point
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import co.touchlab.kermit.Logger
import java.io.ByteArrayOutputStream

fun UiDevice.waitAndFindObject(selector: BySelector, timeoutMs: Long): UiObject2 {
    if (!wait(Until.hasObject(selector), timeoutMs)) {
        Logger.e(dumpWindowHierarchy())
        throw AssertionError("Element not found on screen in ${timeoutMs}ms (selector=$selector)")
    }

    return findObject(selector)
}

fun UiDevice.scrollDown(element: UiObject2) {
    element.setGestureMarginPercentage(0.2f)
    element.drag(Point(element.visibleCenter.x, -element.visibleCenter.y * 2))
    waitForIdle()
}

fun UiDevice.flingDownUp(element: UiObject2) {
    element.setGestureMarginPercentage(0.2f)
    element.fling(Direction.DOWN)
    waitForIdle()
    element.fling(Direction.UP)
}

fun UiDevice.dumpWindowHierarchy(): String {
    val buffer = ByteArrayOutputStream()
    dumpWindowHierarchy(buffer)
    return buffer.toString()
}
