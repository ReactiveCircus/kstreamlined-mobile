package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

public class SettingsPresenter(
    private val settingsDataSource: SettingsDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<SettingsUiEvent, SettingsUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): SettingsUiState {
        var uiState by remember { mutableStateOf<SettingsUiState>(SettingsUiState.Loading) }
        LaunchedEffect(Unit) {
            settingsDataSource.appSettings
                .onEach {
                    uiState = SettingsUiState.Content(
                        theme = it.theme,
                        autoSyncEnabled = it.autoSync,
                        autoSyncInterval = AutoSyncInterval.from(it.autoSyncInterval),
                    )
                }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is SettingsUiEvent.SelectTheme -> {
                    settingsDataSource.updateAppSettings {
                        it.copy(theme = event.theme)
                    }
                }

                is SettingsUiEvent.ToggleAutoSync -> {
                    settingsDataSource.updateAppSettings {
                        it.copy(autoSync = !it.autoSync)
                    }
                }

                is SettingsUiEvent.SelectSyncInterval -> {
                    settingsDataSource.updateAppSettings {
                        it.copy(autoSyncInterval = event.syncInterval.value)
                    }
                }
            }
        }
        return uiState
    }
}
