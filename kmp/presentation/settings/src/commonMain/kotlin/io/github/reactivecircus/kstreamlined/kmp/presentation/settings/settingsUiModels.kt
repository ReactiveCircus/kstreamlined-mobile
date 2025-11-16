package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import androidx.compose.runtime.Immutable
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings

@Immutable
public sealed interface SettingsUiState {
    public data object Loading : SettingsUiState

    public data class Content(
        val appSettings: AppSettings,
    ) : SettingsUiState
}

public sealed interface SettingsUiEvent {
    public data class SelectTheme(val theme: AppSettings.Theme) : SettingsUiEvent
}
