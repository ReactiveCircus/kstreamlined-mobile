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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

public class SettingsPresenter(
    private val settingsDataSource: SettingsDataSource,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<SettingsUiEvent, SettingsUiState>(scope, recompositionMode) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    override fun present(): SettingsUiState {
        var uiState by remember { mutableStateOf<SettingsUiState>(SettingsUiState.Loading) }
        LaunchedEffect(Unit) {
            settingsDataSource.appSettings
                .onEach { uiState = SettingsUiState.Content(appSettings = it) }
                .collect()
        }
        CollectEvent { event ->
            when (event) {
                is SettingsUiEvent.SelectTheme -> {
                    settingsDataSource.updateAppSettings {
                        it.copy(theme = event.theme)
                    }
                }
            }
        }
        return uiState
    }
}
