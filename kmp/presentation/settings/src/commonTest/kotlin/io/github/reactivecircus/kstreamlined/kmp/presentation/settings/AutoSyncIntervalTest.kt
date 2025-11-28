package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AutoSyncIntervalTest {
    @Test
    fun `AutoSyncInterval can be created from Duration matching a known value`() {
        assertEquals(AutoSyncInterval.Hourly, AutoSyncInterval.from(1.hours))
        assertEquals(AutoSyncInterval.Every3Hours, AutoSyncInterval.from(3.hours))
        assertEquals(AutoSyncInterval.Every6Hours, AutoSyncInterval.from(6.hours))
        assertEquals(AutoSyncInterval.Every12Hours, AutoSyncInterval.from(12.hours))
        assertEquals(AutoSyncInterval.Daily, AutoSyncInterval.from(24.hours))
    }

    @Test
    fun `created AutoSyncInterval has default value from AppSettings when Duration does not match a known value`() {
        assertEquals(AppSettings.Default.autoSyncInterval, AutoSyncInterval.from(1.minutes).value)
    }

    @Test
    fun `AutoSyncInterval's Default has expected default value from AppSettings`() {
        assertEquals(AppSettings.Default.autoSyncInterval, AutoSyncInterval.Default.value)
    }
}
