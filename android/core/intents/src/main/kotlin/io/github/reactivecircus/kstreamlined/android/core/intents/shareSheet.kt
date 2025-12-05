package io.github.reactivecircus.kstreamlined.android.core.intents

import android.content.Context
import android.content.Intent

public fun Context.openShareSheet(title: String, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, url)
    }
    startActivity(Intent.createChooser(intent, null))
}
