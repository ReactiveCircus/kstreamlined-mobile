package io.github.reactivecircus.kstreamlined.android.core.web

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.android.core.display.FullScreenEffect
import java.net.URISyntaxException

/**
 * A WebView composable based on
 * https://github.com/google/accompanist/blob/main/web/src/main/java/com/google/accompanist/web/WebView.kt
 *
 * Supports back navigation, HTML5 full screen mode, and opening non-web links (e.g. `intent:`, `mailto:`)
 * in other apps.
 *
 * @param onCreated called when the WebView is created for configuring its settings.
 * WebViewClient and WebChromeClient are overwritten after this is called.
 */
@Composable
public fun WebView(
    state: WebViewState,
    modifier: Modifier = Modifier,
    onCreated: (WebView) -> Unit = {},
) {
    BackHandler(enabled = state.canGoBack) {
        state.webView?.goBack()
    }

    val webView = state.webView
    val url = state.url
    if (webView != null) {
        SideEffect {
            if (state.loadedUrl == url) return@SideEffect
            state.loadedUrl = url
            webView.loadUrl(url)
        }
    }

    AndroidView(
        factory = { context ->
            val retainedWebView = state.webView?.apply {
                (parent as? ViewGroup)?.removeView(this)
                onResume()
            }
            retainedWebView ?: createWebView(context, state, onCreated)
        },
        modifier = modifier,
        onRelease = { it.onPause() },
    )

    WebViewFullScreenHandler(state.fullScreenState)
    if (state.fullScreenState.isFullScreen) {
        FullScreenEffect()
    }
}

/**
 * Creates a [WebViewState] that keeps the WebView (and its loaded page) while the caller is retained,
 * e.g. when navigated away from but still on the back stack.
 */
@Composable
public fun retainWebViewState(url: String): WebViewState {
    val state = retain { WebViewState(url) }
    state.url = url
    RetainedEffect(state) {
        onRetire {
            state.fullScreenState.forceExitFullScreen()
            state.webView?.destroy()
        }
    }
    return state
}

/**
 * The state holder of a [WebView]. Use [retainWebViewState] to create an instance.
 */
@Stable
public class WebViewState internal constructor(url: String) {
    /**
     * Whether the WebView is loading a page in its main frame.
     */
    public var isLoading: Boolean by mutableStateOf(true)
        internal set

    internal var url: String by mutableStateOf(url)

    // The url most recently loaded into [webView], so a re-attached WebView isn't reloaded.
    internal var loadedUrl: String? = null

    internal var canGoBack: Boolean by mutableStateOf(false)

    internal val fullScreenState: WebViewFullScreenState = WebViewFullScreenState()

    internal var webView: WebView? by mutableStateOf(null)
}

private fun createWebView(
    context: Context,
    state: WebViewState,
    onCreated: (WebView) -> Unit,
): WebView = WebView(context).apply {
    // WebView ignores percentage heights in pages when its height is WRAP_CONTENT
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    onCreated(this)
    webViewClient = StateAwareWebViewClient(state)
    webChromeClient = StateAwareWebChromeClient(state)
    state.webView = this
}

private class StateAwareWebViewClient(private val state: WebViewState) : WebViewClient() {
    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        state.isLoading = true
    }

    override fun onPageFinished(view: WebView, url: String?) {
        state.isLoading = false
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val uri = request.url
        if (uri.isWebViewLoadable()) return false
        // prevent subframes (e.g. ads) from launching other apps without user interaction
        if (request.isForMainFrame || request.hasGesture()) {
            view.launchExternalApp(uri)
        }
        return true
    }

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        state.canGoBack = view.canGoBack()
    }
}

private class StateAwareWebChromeClient(private val state: WebViewState) : WebChromeClient() {
    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        state.fullScreenState.onShowCustomView(view, callback, hostView = state.webView)
    }

    override fun onHideCustomView() {
        state.fullScreenState.onHideCustomView()
    }
}

/**
 * Whether the [Uri] can be loaded by the WebView itself, as opposed to schemes that need to be
 * handled by other apps (e.g. `intent:`, `mailto:`, `tel:`, `market:`).
 */
internal fun Uri.isWebViewLoadable(): Boolean = scheme in WebViewLoadableSchemes

/**
 * Opens a [uri] that can't be loaded by the WebView in an app that handles it,
 * falling back to the `browser_fallback_url` of `intent:` URIs.
 * Intents are restricted to browsable activities as they come from untrusted web content.
 */
internal fun WebView.launchExternalApp(uri: Uri) {
    val intent = if (uri.scheme == IntentScheme) {
        try {
            Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME)
        } catch (e: URISyntaxException) {
            Logger.w(e) { "Failed to parse intent URI: $uri" }
            return
        }
    } else {
        Intent(Intent.ACTION_VIEW, uri)
    }
    intent.addCategory(Intent.CATEGORY_BROWSABLE)
    intent.component = null
    intent.selector = null
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val fallbackUri = intent.getStringExtra(BrowserFallbackUrlExtra)?.let(Uri::parse)
        if (fallbackUri != null && fallbackUri.scheme in HttpSchemes) {
            loadUrl(fallbackUri.toString())
        } else {
            Logger.w(e) { "No app found to handle $uri" }
        }
    }
}

private const val IntentScheme = "intent"
private const val BrowserFallbackUrlExtra = "browser_fallback_url"
private val HttpSchemes = setOf("http", "https")
private val WebViewLoadableSchemes = HttpSchemes + setOf("about", "data", "blob", "javascript")
