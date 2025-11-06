package io.github.reactivecircus.kstreamlined.kmp.settings.datasource

import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public class SettingsDataSource {
    public fun streamAppSettings(): Flow<AppSettings> {
        return flow { AppSettings.Default }
    }

    public suspend fun updateAppSettings(appSettings: AppSettings) {
        TODO()
    }
}
