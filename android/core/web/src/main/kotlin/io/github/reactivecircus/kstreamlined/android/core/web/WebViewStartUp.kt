package io.github.reactivecircus.kstreamlined.android.core.web

import android.content.Context
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewOutcomeReceiver
import androidx.webkit.WebViewStartUpConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/**
 * Asynchronously triggers WebView start-up to improve first usage performance.
 */
public fun Context.startUpWebViewAsync() {
    val config = WebViewStartUpConfig.Builder(Dispatchers.Default.asExecutor()).build()
    WebViewCompat.startUpWebView(applicationContext, config, WebViewOutcomeReceiver {})
}
