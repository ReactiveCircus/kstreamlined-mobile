package io.github.reactivecircus.kstreamlined.android.core.display

import android.view.OrientationEventListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs

/**
 * Calls [onChange] when the device is physically rotated to a different [DeviceOrientation] while in composition,
 * regardless of the screen orientation (e.g. when it's locked via [ScreenOrientationEffect]).
 * The orientation when entering composition is not reported.
 *
 * This is a no-op on devices without an orientation sensor.
 */
@Composable
public fun DeviceOrientationChangeEffect(onChange: (DeviceOrientation) -> Unit) {
    val context = LocalContext.current
    val currentOnChange by rememberUpdatedState(onChange)
    DisposableEffect(context) {
        val listener = object : OrientationEventListener(context) {
            private var orientation: DeviceOrientation? = null

            override fun onOrientationChanged(degrees: Int) {
                val newOrientation = when {
                    degrees == ORIENTATION_UNKNOWN -> return
                    degrees.isNear(0) || degrees.isNear(360) || degrees.isNear(180) -> DeviceOrientation.Portrait
                    degrees.isNear(90) || degrees.isNear(270) -> DeviceOrientation.Landscape
                    else -> return
                }
                val previousOrientation = orientation
                orientation = newOrientation
                if (previousOrientation != null && previousOrientation != newOrientation) {
                    currentOnChange(newOrientation)
                }
            }

            private fun Int.isNear(degrees: Int) = abs(this - degrees) <= ToleranceDegrees
        }
        if (listener.canDetectOrientation()) {
            listener.enable()
        }
        onDispose { listener.disable() }
    }
}

/**
 * The orientation the device is physically held in, regardless of the screen orientation.
 */
public enum class DeviceOrientation {
    Portrait,
    Landscape,
}

private const val ToleranceDegrees = 20
