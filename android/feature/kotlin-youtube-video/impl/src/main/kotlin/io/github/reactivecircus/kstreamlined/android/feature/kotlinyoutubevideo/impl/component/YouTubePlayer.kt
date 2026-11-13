package io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import io.github.reactivecircus.kstreamlined.android.core.web.WebViewFullScreenHandler
import io.github.reactivecircus.kstreamlined.android.core.web.WebViewFullScreenState

/**
 * Plays a YouTube video using the YouTube IFrame Player API in a [WebView].
 * The video starts playing automatically.
 */
@Composable
internal fun YouTubePlayer(
    videoId: String,
    fullScreenState: WebViewFullScreenState,
    onReady: () -> Unit,
    onError: (YouTubePlayerError) -> Unit,
    onOpenUrl: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier)
    } else {
        val currentOnReady by rememberUpdatedState(onReady)
        val currentOnError by rememberUpdatedState(onError)
        val currentOnOpenUrl by rememberUpdatedState(onOpenUrl)
        AndroidView(
            factory = { context ->
                createPlayerWebView(
                    context = context,
                    videoId = videoId,
                    fullScreenState = fullScreenState,
                    onReady = { currentOnReady() },
                    onError = { currentOnError(it) },
                    onOpenUrl = { currentOnOpenUrl(it) },
                )
            },
            modifier = modifier,
            onRelease = {
                fullScreenState.forceExitFullScreen()
                it.destroy()
            },
        )
        WebViewFullScreenHandler(fullScreenState)
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createPlayerWebView(
    context: Context,
    videoId: String,
    fullScreenState: WebViewFullScreenState,
    onReady: () -> Unit,
    onError: (YouTubePlayerError) -> Unit,
    onOpenUrl: (url: String) -> Unit,
): WebView = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    setBackgroundColor(Color.TRANSPARENT)
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.mediaPlaybackRequiresUserGesture = false // allow autoplay
    webChromeClient = object : WebChromeClient() {
        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            fullScreenState.onShowCustomView(view, callback, hostView = this@apply)
        }

        override fun onHideCustomView() {
            fullScreenState.onHideCustomView()
        }
    }
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            if (!request.isForMainFrame) return false
            onOpenUrl(request.url.toString())
            return true
        }
    }
    addJavascriptInterface(
        PlayerBridge(
            onReady = { post(onReady) },
            onError = { error -> post { onError(error) } },
        ),
        PlayerBridge.Name,
    )
    // identify the app via the Referer
    val origin = "https://${context.packageName}"
    loadDataWithBaseURL(origin, playerHtml(videoId = videoId, origin = origin), "text/html", "utf-8", null)
}

internal enum class YouTubePlayerError {
    /**
     * The player couldn't be loaded, e.g. no network connection. Retrying may succeed.
     */
    LoadFailed,

    /**
     * The video can't be played in the player, e.g. it was removed or doesn't allow embedded playback.
     */
    VideoUnavailable,
}

private class PlayerBridge(
    private val onReady: () -> Unit,
    private val onError: (YouTubePlayerError) -> Unit,
) {
    @JavascriptInterface
    fun onReady() = onReady.invoke()

    @JavascriptInterface
    fun onLoadError() = onError(YouTubePlayerError.LoadFailed)

    /**
     * See https://developers.google.com/youtube/iframe_api_reference#onError for the error codes.
     */
    @JavascriptInterface
    fun onPlayerError(errorCode: Int) = onError(
        if (errorCode == HTML5PlayerErrorCode) YouTubePlayerError.LoadFailed else YouTubePlayerError.VideoUnavailable,
    )

    companion object {
        const val Name = "KSYouTubePlayer"
    }
}

private fun playerHtml(videoId: String, origin: String): String = """
    <!DOCTYPE html>
    <html>
    <head>
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <style>
        html, body { margin: 0; padding: 0; width: 100%; height: 100%; overflow: hidden; background-color: transparent; }
        #player { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }
      </style>
    </head>
    <body>
      <div id="player"></div>
      <script>
        var isReady = false;
        function notifyReady() {
          if (isReady) return;
          isReady = true;
          ${PlayerBridge.Name}.onReady();
        }
        function onYouTubeIframeAPIReady() {
          new YT.Player('player', {
            width: '100%',
            height: '100%',
            videoId: '$videoId',
            playerVars: { playsinline: 1, rel: 0, origin: '$origin' },
            events: {
              onReady: function(event) {
                event.target.playVideo();
                // display the player even if autoplay doesn't start
                setTimeout(notifyReady, $AutoplayTimeoutMillis);
              },
              onStateChange: function(event) {
                if (event.data === YT.PlayerState.PLAYING) notifyReady();
              },
              onError: function(event) { ${PlayerBridge.Name}.onPlayerError(event.data); }
            }
          });
        }
      </script>
      <script src="https://www.youtube.com/iframe_api" onerror="${PlayerBridge.Name}.onLoadError()"></script>
    </body>
    </html>
""".trimIndent()

private const val AutoplayTimeoutMillis = 3000
private const val HTML5PlayerErrorCode = 5
