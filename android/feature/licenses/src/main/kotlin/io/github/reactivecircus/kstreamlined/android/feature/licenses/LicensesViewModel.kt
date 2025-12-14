package io.github.reactivecircus.kstreamlined.android.feature.licenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import io.github.reactivecircus.licentia.runtime.LicensesInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class LicensesViewModel @Inject constructor(
    licensesInfo: LicensesInfo,
) : ViewModel() {
    private val presenter = LicensesPresenter(
        licensesInfo = licensesInfo,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
        recompositionMode = RecompositionMode.ContextClock,
    )
    val uiState: StateFlow<LicensesUiState> = presenter.states
}

internal class LicensesPresenter(
    private val licensesInfo: LicensesInfo,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode,
) : Presenter<LicensesUiEvent, LicensesUiState>(scope, recompositionMode) {
    @Composable
    override fun present(): LicensesUiState {
        var uiState by remember { mutableStateOf<LicensesUiState>(LicensesUiState.Loading) }
        DisposableEffect(Unit) {
            uiState = LicensesUiState.Content(
                items = licensesInfo.artifacts.toArtifactLicenseItems(),
            )
            onDispose { }
        }
        return uiState
    }
}
