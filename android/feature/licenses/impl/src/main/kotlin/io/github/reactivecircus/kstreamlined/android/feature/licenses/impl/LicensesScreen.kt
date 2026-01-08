package io.github.reactivecircus.kstreamlined.android.feature.licenses.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.tracing.trace
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.licenses.api.LicensesSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.licenses.impl.component.ArtifactLicenseRow

@Composable
internal fun SharedTransitionScope.LicensesScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
) = trace("Screen:Licenses") {
    val viewModel = metroViewModel<LicensesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LicensesScreen(
        onNavigateUp = backStack::removeLastOrNull,
        uiState = uiState,
        modifier = modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(key = LicensesSharedTransitionKeys.Bounds),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
    )
}

@Composable
internal fun SharedTransitionScope.LicensesScreen(
    onNavigateUp: () -> Unit,
    uiState: LicensesUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            title = stringResource(id = R.string.title_licenses),
            modifier = Modifier.zIndex(1f),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
        )

        AnimatedVisibility(
            visible = uiState is LicensesUiState.Content,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (uiState is LicensesUiState.Content) {
                ContentUi(state = uiState)
            }
        }
    }
}

@Composable
private fun ContentUi(
    state: LicensesUiState.Content,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        state.items.forEach {
            item {
                ArtifactLicenseRow(item = it)
            }
        }
    }
}
