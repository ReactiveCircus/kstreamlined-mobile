package io.github.reactivecircus.kstreamlined.android.core.web

import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Hosts the custom view of a page in HTML5 full screen mode (e.g. a video player's full screen button).
 *
 * [onShowCustomView] and [onHideCustomView] should be called from the corresponding [WebChromeClient] callbacks.
 */
@Stable
public class WebViewFullScreenState {
    private var customView: CustomView? by mutableStateOf(null)

    /**
     * Whether the page is currently displaying an element in full screen mode.
     */
    public val isFullScreen: Boolean
        get() = customView != null

    /**
     * Displays the custom [view] on top of the window that [hostView] is attached to.
     */
    public fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback, hostView: WebView?) {
        val container = hostView?.rootView as? ViewGroup
        if (customView != null || container == null) {
            callback.onCustomViewHidden()
            return
        }
        // The view must be attached to a window synchronously, otherwise WebView considers the page hidden
        // and pauses any playing media.
        container.addView(
            view,
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
        )
        customView = CustomView(
            view = view,
            callback = callback,
            hostView = hostView,
            hostScrollX = hostView.scrollX,
            hostScrollY = hostView.scrollY,
        )
    }

    public fun onHideCustomView() {
        val current = customView ?: return
        removeCustomView()
        current.restoreHostScrollPosition()
    }

    /**
     * Requests the page to exit full screen mode. The custom view is removed in [onHideCustomView].
     */
    public fun exitFullScreen() {
        customView?.callback?.onCustomViewHidden()
    }

    /**
     * Exits full screen mode and removes the custom view immediately.
     * This should be called before destroying the WebView, after which [onHideCustomView] is never called.
     */
    public fun forceExitFullScreen() {
        exitFullScreen()
        removeCustomView()
    }

    private fun removeCustomView() {
        customView?.view?.let { (it.parent as? ViewGroup)?.removeView(it) }
        customView = null
    }

    private class CustomView(
        val view: View,
        val callback: WebChromeClient.CustomViewCallback,
        val hostView: WebView,
        val hostScrollX: Int,
        val hostScrollY: Int,
    ) {
        // WebView scrolls the page to the top when exiting full screen mode,
        // so the page restores the scroll position once it has exited full screen mode.
        fun restoreHostScrollPosition() {
            hostView.evaluateJavascript(restoreScrollScript(hostScrollX, hostScrollY), null)
        }
    }
}

/**
 * Scrolls the page to the WebView scroll position (in pixels) once the page has exited full screen mode.
 */
private fun restoreScrollScript(scrollX: Int, scrollY: Int): String = """
    (function(x, y) {
      function restore() {
        var scale = window.devicePixelRatio * (window.visualViewport ? window.visualViewport.scale : 1);
        window.scrollTo(x / scale, y / scale);
      }
      if (!document.fullscreenElement) {
        requestAnimationFrame(restore);
        return;
      }
      document.addEventListener('fullscreenchange', function onChange() {
        if (document.fullscreenElement) return;
        document.removeEventListener('fullscreenchange', onChange);
        restore();
      });
    })($scrollX, $scrollY);
""".trimIndent()
