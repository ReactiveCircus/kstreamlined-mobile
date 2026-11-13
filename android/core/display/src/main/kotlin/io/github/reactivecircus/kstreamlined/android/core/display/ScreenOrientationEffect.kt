package io.github.reactivecircus.kstreamlined.android.core.display

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Requests the screen [orientation] (one of `ActivityInfo.SCREEN_ORIENTATION_*`) while in composition.
 * Only one should be active at a time.
 */
@Composable
public fun ScreenOrientationEffect(orientation: Int) {
    val activity = LocalActivity.current ?: return
    DisposableEffect(activity, orientation) {
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose { activity.requestedOrientation = originalOrientation }
    }
}
