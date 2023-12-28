package io.github.reactivecircus.kstreamlined.android.feature.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import co.touchlab.kermit.Logger

public fun Context.openCustomTab(url: String) {
    try {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, Uri.parse(url))
    } catch (e: ActivityNotFoundException) {
        Logger.w(e) { "No browser installed." }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
