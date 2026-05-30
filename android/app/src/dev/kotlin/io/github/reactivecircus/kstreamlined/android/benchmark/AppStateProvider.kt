package io.github.reactivecircus.kstreamlined.android.benchmark

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import io.github.reactivecircus.kstreamlined.android.KSApp
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import kotlinx.coroutines.runBlocking

class AppStateProvider : ContentProvider() {
    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val app = context!!.applicationContext as KSApp
        when (method) {
            "resetFeedSelections" -> runBlocking {
                app.appGraph.feedDataSource.selectAllFeedSources()
            }

            "selectSingleFeed" -> runBlocking {
                app.appGraph.feedDataSource.selectSingleFeedSource(FeedOrigin.Key.valueOf(arg!!))
            }
        }
        return null
    }

    override fun onCreate(): Boolean = true
    override fun query(uri: Uri, p: Array<String>?, s: String?, sa: Array<String>?, so: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}
