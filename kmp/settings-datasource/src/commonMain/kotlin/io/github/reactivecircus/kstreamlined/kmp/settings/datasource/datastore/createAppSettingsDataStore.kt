package io.github.reactivecircus.kstreamlined.kmp.settings.datasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import okio.Path.Companion.toPath

internal fun createAppSettingsDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
        produceFile = { producePath().toPath() },
    )

internal const val AppSettingsDataStoreFileName = "app_settings.preferences_pb"

internal object AppSettingsDataStoreKeys {
    val Theme = stringPreferencesKey("theme")
    val AutoSync = booleanPreferencesKey("auto_sync")
    val AutoSyncIntervalInMillis = longPreferencesKey("auto_sync_interval_in_millis")
}
