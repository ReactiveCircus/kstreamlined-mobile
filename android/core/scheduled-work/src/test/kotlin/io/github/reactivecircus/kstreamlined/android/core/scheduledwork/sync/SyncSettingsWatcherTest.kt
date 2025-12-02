package io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.datastore.testing.createFakeDataStore
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class SyncSettingsWatcherTest {
    private val testScope = TestScope(StandardTestDispatcher())

    private val settingsDataStore = createFakeDataStore(testScope)

    private val settingsDataSource = SettingsDataSource(settingsDataStore)

    @Test
    fun `initial SyncSettingsChangeEvent emitted after debounce delay`() =
        testScope.runTest {
            settingsDataSource.appSettings.syncSettingsChanges().test {
                advanceTimeBy(SettingsChangeDebounce)

                expectNoEvents()

                advanceTimeBy(1.milliseconds)

                assertEquals(
                    SyncSettingsChangeEvent(
                        enabled = AppSettings.Default.autoSync,
                        syncInterval = AppSettings.Default.autoSyncInterval,
                    ),
                    expectMostRecentItem(),
                )

                expectNoEvents()
            }
        }

    @Test
    fun `sync settings changes are debounced`() = testScope.runTest {
        settingsDataSource.appSettings.syncSettingsChanges().test {
            skipItems(1)

            settingsDataSource.updateAppSettings {
                it.copy(autoSyncInterval = 1.hours)
            }

            settingsDataSource.updateAppSettings {
                it.copy(autoSyncInterval = 2.hours)
            }

            advanceTimeBy(1.milliseconds)

            settingsDataSource.updateAppSettings {
                it.copy(autoSyncInterval = 3.hours)
            }

            expectNoEvents()

            advanceTimeBy(SettingsChangeDebounce + 1.milliseconds)

            assertEquals(
                SyncSettingsChangeEvent(
                    enabled = AppSettings.Default.autoSync,
                    syncInterval = 3.hours,
                ),
                expectMostRecentItem(),
            )

            expectNoEvents()
        }
    }

    @Test
    fun `non-sync settings changes are ignored`() = testScope.runTest {
        settingsDataSource.appSettings.syncSettingsChanges().test {
            skipItems(1)

            settingsDataSource.updateAppSettings {
                it.copy(theme = AppSettings.Theme.Dark)
            }

            advanceTimeBy(SettingsChangeDebounce + 1.milliseconds)

            expectNoEvents()
        }
    }
}
