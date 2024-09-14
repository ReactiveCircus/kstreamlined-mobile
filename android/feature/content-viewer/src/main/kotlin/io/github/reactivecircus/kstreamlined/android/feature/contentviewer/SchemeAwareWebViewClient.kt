package io.github.reactivecircus.kstreamlined.android.feature.contentviewer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import co.touchlab.kermit.Logger

internal class SchemeAwareWebViewClient : AccompanistWebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        request?.let {
            var url = it.url.toString()
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return false
            }
            if (url.contains("youtube.com")) {
                url = url.replaceFirst("intent://", "vnd.youtube:")
            }
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view?.context?.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Logger.e(e) { "No Activity was found to handle the Intent." }
            }
        }
        return true
    }
}
