package io.github.reactivecircus.kstreamlined.android.core.launcher

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import co.touchlab.kermit.Logger

public fun Context.openCustomTab(url: String) {
    try {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, url.toUri())
    } catch (e: ActivityNotFoundException) {
        Logger.w(e) { "No browser installed." }
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}
