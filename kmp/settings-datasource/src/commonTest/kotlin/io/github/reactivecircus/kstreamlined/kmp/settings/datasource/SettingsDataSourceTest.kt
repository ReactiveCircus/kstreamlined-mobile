package io.github.reactivecircus.kstreamlined.kmp.settings.datasource

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.datastore.testing.createFakeDataStore
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class SettingsDataSourceTest {
    private val settingsDataSource = SettingsDataSource(createFakeDataStore())

    @Test
    fun `appSettings Flow emits default AppSettings when DataStore is empty`() = runTest {
        settingsDataSource.appSettings.test {
            assertEquals(AppSettings.Default, awaitItem())
        }
    }

    @Test
    fun `appSettings Flow emits when updateAppSettings changes AppSettings in DataStore`() =
        runTest {
            settingsDataSource.updateAppSettings {
                it.copy(autoSync = false)
            }

            settingsDataSource.appSettings.test {
                assertEquals(AppSettings.Default.copy(autoSync = false), awaitItem())

                settingsDataSource.updateAppSettings {
                    it.copy(
                        theme = AppSettings.Theme.Dark,
                        autoSync = true,
                        autoSyncInterval = 30.minutes,
                    )
                }

                settingsDataSource.updateAppSettings {
                    it.copy(
                        theme = AppSettings.Theme.Light,
                        autoSync = false,
                        autoSyncInterval = 1.hours,
                    )
                }

                assertEquals(
                    AppSettings(
                        theme = AppSettings.Theme.Dark,
                        autoSync = true,
                        autoSyncInterval = 30.minutes,
                    ),
                    awaitItem(),
                )
                assertEquals(
                    AppSettings(
                        theme = AppSettings.Theme.Light,
                        autoSync = false,
                        autoSyncInterval = 1.hours,
                    ),
                    awaitItem(),
                )
            }
        }
}
