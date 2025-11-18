package io.github.reactivecircus.kstreamlined.kmp.settings.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.datastore.AppSettingsDataStoreKeys
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.DurationUnit
import kotlin.time.toDuration

public class SettingsDataSource(private val dataStore: DataStore<Preferences>) {
    public val appSettings: Flow<AppSettings> = dataStore.data.map { it.toData() }

    public suspend fun updateAppSettings(transform: suspend (appSettings: AppSettings) -> AppSettings) {
        dataStore.updateData {
            transform(it.toData()).toPreferences(it.toMutablePreferences())
        }
    }

    private fun AppSettings.toPreferences(mutablePreferences: MutablePreferences): Preferences {
        return mutablePreferences.apply {
            this[AppSettingsDataStoreKeys.Theme] = theme.name
            this[AppSettingsDataStoreKeys.AutoSync] = autoSync
            this[AppSettingsDataStoreKeys.AutoSyncIntervalInMillis] = autoSyncInterval.inWholeMilliseconds
        }.toPreferences()
    }

    private fun Preferences.toData(): AppSettings {
        return AppSettings(
            theme = this[AppSettingsDataStoreKeys.Theme]?.let {
                AppSettings.Theme.valueOf(it)
            } ?: AppSettings.Default.theme,
            autoSync = this[AppSettingsDataStoreKeys.AutoSync] ?: AppSettings.Default.autoSync,
            autoSyncInterval = this[AppSettingsDataStoreKeys.AutoSyncIntervalInMillis]
                ?.toDuration(DurationUnit.MILLISECONDS)
                ?: AppSettings.Default.autoSyncInterval,
        )
    }
}
