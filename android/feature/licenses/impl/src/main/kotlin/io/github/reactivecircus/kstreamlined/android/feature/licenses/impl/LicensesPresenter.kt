package io.github.reactivecircus.kstreamlined.android.feature.licenses.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterKey
import io.github.reactivecircus.licentia.runtime.LicensesInfo

@PresenterKey
@ContributesIntoMap(AppScope::class)
internal class LicensesPresenter(
    private val licensesInfo: LicensesInfo,
    moleculeContext: MoleculeContext,
) : Presenter<LicensesUiEvent, LicensesUiState>(moleculeContext) {
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
