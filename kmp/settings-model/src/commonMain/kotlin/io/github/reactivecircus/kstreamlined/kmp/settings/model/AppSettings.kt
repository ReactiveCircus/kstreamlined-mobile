package io.github.reactivecircus.kstreamlined.kmp.settings.model

import androidx.compose.runtime.Immutable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Immutable
public data class AppSettings(
    public val theme: Theme,
    public val autoSync: Boolean,
    public val autoSyncInterval: Duration,
) {
    @Immutable
    public enum class Theme {
        System,
        Light,
        Dark,
    }

    public companion object {
        public val Default: AppSettings = AppSettings(
            theme = Theme.System,
            autoSync = true,
            autoSyncInterval = 6.hours,
        )
    }
}
