package io.github.reactivecircus.kstreamlined.kmp.settings.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

public fun createAppSettingsDataStore(context: Context): DataStore<Preferences> {
    return createAppSettingsDataStore(
        producePath = {
            context.filesDir.resolve(AppSettingsDataStoreFileName).absolutePath
        },
    )
}
