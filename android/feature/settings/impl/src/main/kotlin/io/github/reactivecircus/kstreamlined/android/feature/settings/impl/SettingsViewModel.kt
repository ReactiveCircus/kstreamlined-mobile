package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.appinfo.AppInfo
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(SettingsViewModel::class)
@ContributesIntoMap(AppScope::class)
public class SettingsViewModel(
    settingsDataSource: SettingsDataSource,
    appInfo: AppInfo,
) : ViewModel() {
    private val presenter = SettingsPresenter(
        settingsDataSource = settingsDataSource,
        appInfo = appInfo,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
    internal val uiState: StateFlow<SettingsUiState> = presenter.states
    internal val eventSink: (SettingsUiEvent) -> Unit = presenter.eventSink
}
