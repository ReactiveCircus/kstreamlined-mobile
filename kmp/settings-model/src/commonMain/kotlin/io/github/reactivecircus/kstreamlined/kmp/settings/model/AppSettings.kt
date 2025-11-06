package io.github.reactivecircus.kstreamlined.kmp.settings.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

public class AppSettings(
    public val theme: Theme,
    public val autoSync: Boolean,
    public val autoSyncInterval: Duration,
) {
    public enum class Theme {
        Light,
        Dark,
        System;
    }

    public companion object {
        public val Default: AppSettings = AppSettings(
            theme = Theme.System,
            autoSync = true,
            autoSyncInterval = 6.hours,
        )
    }
}
