package io.github.reactivecircus.kstreamlined.android.core.display

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Hides the system bars while in composition. Only one should be active at a time.
 */
@Composable
public fun FullScreenEffect() {
    val activity = LocalActivity.current ?: return
    DisposableEffect(activity) {
        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        val originalBehavior = insetsController.systemBarsBehavior
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        onDispose {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            insetsController.systemBarsBehavior = originalBehavior
        }
    }
}
