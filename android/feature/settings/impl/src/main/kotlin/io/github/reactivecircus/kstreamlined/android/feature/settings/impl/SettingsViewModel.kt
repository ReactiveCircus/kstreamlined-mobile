package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.appinfo.AppInfo
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    settingsDataSource: SettingsDataSource,
    appInfo: AppInfo,
) : ViewModel() {
    private val presenter = SettingsPresenter(
        settingsDataSource = settingsDataSource,
        appInfo = appInfo,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
        recompositionMode = RecompositionMode.ContextClock,
    )
    val uiState: StateFlow<SettingsUiState> = presenter.states
    val eventSink: (SettingsUiEvent) -> Unit = presenter.eventSink
}
