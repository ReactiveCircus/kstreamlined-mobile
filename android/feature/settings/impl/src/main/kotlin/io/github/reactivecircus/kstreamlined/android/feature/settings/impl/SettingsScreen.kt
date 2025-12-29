package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.ModalBottomSheet
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.rememberModalBottomSheetState
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.licenses.api.LicensesSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.about.OpenSourceLicensesTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.about.SourceCodeTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.about.VersionTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.sync.AutoSyncSwitch
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.sync.SyncIntervalPicker
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.sync.SyncIntervalTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.theme.ThemeSelector
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import kotlinx.coroutines.launch

@Composable
internal fun SharedTransitionScope.SettingsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    topBarBoundsKey: String,
    titleElementKey: String,
    onOpenLicenses: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("Screen:Settings") {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    SettingsScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        topBarBoundsKey = topBarBoundsKey,
        titleElementKey = titleElementKey,
        onOpenLicenses = onOpenLicenses,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier,
    )
}

@Composable
internal fun SharedTransitionScope.SettingsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    topBarBoundsKey: String,
    titleElementKey: String,
    onOpenLicenses: () -> Unit,
    onNavigateUp: () -> Unit,
    uiState: SettingsUiState,
    eventSink: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
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
            title = stringResource(id = R.string.title_settings),
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
                    animatedVisibilityScope = animatedVisibilityScope,
                    state = uiState,
                    eventSink = eventSink,
                    onOpenLicenses = onOpenLicenses,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SharedTransitionScope.ContentUi(
    animatedVisibilityScope: AnimatedVisibilityScope,
    state: SettingsUiState.Content,
    eventSink: (SettingsUiEvent) -> Unit,
    onOpenLicenses: () -> Unit,
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
                selectedTheme = state.theme,
                onSelectTheme = { eventSink(SettingsUiEvent.SelectTheme(it)) },
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Column(modifier = Modifier.animateContentSize()) {
                Text(
                    text = stringResource(R.string.section_heading_data_sync),
                    style = KSTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = KSTheme.colorScheme.onBackgroundVariant,
                )

                Spacer(modifier = Modifier.height(16.dp))

                AutoSyncSwitch(
                    selected = state.autoSyncEnabled,
                    onSelectedChange = { eventSink(SettingsUiEvent.ToggleAutoSync) },
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (state.autoSyncEnabled) {
                    var openSyncIntervalPicker by rememberSaveable { mutableStateOf(false) }

                    SyncIntervalTile(
                        selectedSyncInterval = state.autoSyncInterval,
                        onClick = { openSyncIntervalPicker = true },
                    )

                    val syncIntervalSheetState = rememberModalBottomSheetState()
                    if (openSyncIntervalPicker) {
                        val scope = rememberCoroutineScope()
                        ModalBottomSheet(
                            onDismissRequest = { openSyncIntervalPicker = false },
                            sheetState = syncIntervalSheetState,
                        ) {
                            SyncIntervalPicker(
                                selectedSyncInterval = state.autoSyncInterval,
                                onSelectSyncInterval = {
                                    scope.launch { syncIntervalSheetState.hide() }
                                        .invokeOnCompletion {
                                            if (!syncIntervalSheetState.isVisible) {
                                                openSyncIntervalPicker = false
                                            }
                                        }
                                    eventSink(SettingsUiEvent.SelectSyncInterval(it))
                                },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Text(
                text = stringResource(R.string.section_heading_about),
                style = KSTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = KSTheme.colorScheme.onBackgroundVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SourceCodeTile(sourceCodeUrl = state.sourceCodeUrl)

            Spacer(modifier = Modifier.height(8.dp))

            OpenSourceLicensesTile(
                onClick = onOpenLicenses,
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = LicensesSharedTransitionKeys.Bounds),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            VersionTile(version = state.versionName)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
