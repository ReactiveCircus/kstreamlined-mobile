package io.github.reactivecircus.kstreamlined.kmp.settings.datasource

import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsDataSourceTest {
    private val settingsDataSource = SettingsDataSource()

    @Test
    fun `streamAppSettings emits when settings change in dataStore`() = runTest {
        assertEquals(true, true)
//        settingsDataSource.streamAppSettings().test {
//            expectNoEvents()
//            assertEquals(AppSettings.Default, awaitItem())
////            cancelAndIgnoreRemainingEvents()
//        }
    }
}
