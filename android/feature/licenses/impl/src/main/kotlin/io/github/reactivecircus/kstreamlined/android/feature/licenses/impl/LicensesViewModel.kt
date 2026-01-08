package io.github.reactivecircus.kstreamlined.android.feature.licenses.impl

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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.reactivecircus.kstreamlined.kmp.presentation.common.Presenter
import io.github.reactivecircus.licentia.runtime.LicensesInfo
import kotlinx.coroutines.CoroutineScope

@Inject
@ViewModelKey(LicensesViewModel::class)
@ContributesIntoMap(AppScope::class)
public class LicensesViewModel(
    licensesInfo: LicensesInfo,
) : ViewModel() {
    internal val presenter = LicensesPresenter(
        licensesInfo = licensesInfo,
        scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main),
    )
}

internal class LicensesPresenter(
    private val licensesInfo: LicensesInfo,
    scope: CoroutineScope,
    recompositionMode: RecompositionMode = RecompositionMode.ContextClock,
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
