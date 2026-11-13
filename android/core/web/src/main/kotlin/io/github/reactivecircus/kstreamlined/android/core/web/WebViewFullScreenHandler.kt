package io.github.reactivecircus.kstreamlined.android.core.web

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Exits full screen mode on back press and when leaving composition.
 * The caller is responsible for hiding the system bars while in full screen mode.
 */
@Composable
public fun WebViewFullScreenHandler(state: WebViewFullScreenState) {
    BackHandler(enabled = state.isFullScreen, onBack = state::exitFullScreen)
    DisposableEffect(state) {
        onDispose { state.forceExitFullScreen() }
    }
}
