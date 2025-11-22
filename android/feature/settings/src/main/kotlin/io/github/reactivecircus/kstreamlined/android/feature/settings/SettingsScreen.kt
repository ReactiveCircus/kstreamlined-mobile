@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.AutoSyncSwitch
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.ThemeSelector
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.HorizontalDivider
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings

@Composable
public fun SharedTransitionScope.SettingsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsKey: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:Settings") {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    val title = stringResource(id = R.string.title_settings)
    SettingsScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        title = title,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier.sharedBounds(
            rememberSharedContentState(key = boundsKey),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
    )
}

@Composable
internal fun SharedTransitionScope.SettingsScreen(
    title: String,
    onNavigateUp: () -> Unit,
    uiState: SettingsUiState,
    eventSink: (SettingsUiEvent) -> Unit,
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
            title = title,
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
            visible = uiState is SettingsUiState.Content,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (uiState is SettingsUiState.Content) {
                ContentUi(
                    appSettings = uiState.appSettings,
                    eventSink = eventSink,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentUi(
    appSettings: AppSettings,
    eventSink: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.section_heading_appearance),
                style = KSTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = KSTheme.colorScheme.onBackgroundVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ThemeSelector(
                selectedTheme = appSettings.theme,
                onSelectTheme = { eventSink(SettingsUiEvent.SelectTheme(it)) },
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()
        }
        item {
            Text(
                text = stringResource(R.string.section_heading_data_sync),
                style = KSTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = KSTheme.colorScheme.onBackgroundVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AutoSyncSwitch(
                selected = appSettings.autoSync,
                onSelectedChange = { eventSink(SettingsUiEvent.ToggleAutoSync) },
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
