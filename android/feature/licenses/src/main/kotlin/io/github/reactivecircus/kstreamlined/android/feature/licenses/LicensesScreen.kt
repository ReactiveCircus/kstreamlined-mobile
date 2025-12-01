@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.feature.licenses.component.ArtifactLicenseRow
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons

@Composable
public fun SharedTransitionScope.LicensesScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    topBarBoundsKey: String,
    titleElementKey: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:Licenses") {
    val viewModel = hiltViewModel<LicensesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LicensesScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        topBarBoundsKey = topBarBoundsKey,
        titleElementKey = titleElementKey,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
internal fun SharedTransitionScope.LicensesScreen(
    topBarBoundsKey: String,
    titleElementKey: String,
    onNavigateUp: () -> Unit,
    uiState: LicensesUiState,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = topBarBoundsKey,
            titleElementKey = titleElementKey,
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
