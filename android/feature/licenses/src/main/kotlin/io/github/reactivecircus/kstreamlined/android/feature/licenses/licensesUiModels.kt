package io.github.reactivecircus.kstreamlined.android.feature.licenses

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface LicensesUiState {
    data object Loading : LicensesUiState

    data class Content(
        val items: List<ArtifactLicenseItem>,
    ) : LicensesUiState
}

internal sealed interface LicensesUiEvent

@Immutable
internal data class ArtifactLicenseItem(
    val title: String,
    val description: String,
    val version: String,
    val scmUrl: String?,
    val licenses: List<String>,
)
