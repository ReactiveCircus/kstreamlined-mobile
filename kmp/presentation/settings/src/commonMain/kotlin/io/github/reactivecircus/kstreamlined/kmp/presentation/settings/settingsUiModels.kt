package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Immutable
public sealed interface SettingsUiState {
    public data object Loading : SettingsUiState

    public data class Content(
        val theme: AppSettings.Theme,
        val autoSyncEnabled: Boolean,
        val autoSyncInterval: AutoSyncInterval,
        val versionName: String,
        val sourceCodeUrl: String,
    ) : SettingsUiState
}

public sealed interface SettingsUiEvent {
    public data class SelectTheme(val theme: AppSettings.Theme) : SettingsUiEvent

    public data object ToggleAutoSync : SettingsUiEvent

    public data class SelectSyncInterval(
        val syncInterval: AutoSyncInterval,
    ) : SettingsUiEvent
}

@Immutable
public enum class AutoSyncInterval(public val value: Duration) {
    Hourly(1.hours),
    Every3Hours(3.hours),
    Every6Hours(6.hours),
    Every12Hours(12.hours),
    Daily(24.hours),
    ;

    internal companion object {
        val Default = from(AppSettings.Default.autoSyncInterval)

        fun from(duration: Duration): AutoSyncInterval {
            return entries.firstOrNull { it.value == duration }
                ?: entries.first { it.value == AppSettings.Default.autoSyncInterval }
        }
    }
}
