@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.about.OpenSourceLicensesTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.about.SourceCodeTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.about.VersionTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync.AutoSyncSwitch
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync.SyncIntervalPicker
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync.SyncIntervalTile
import io.github.reactivecircus.kstreamlined.android.feature.settings.component.theme.ThemeSelector
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.ModalBottomSheet
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.rememberModalBottomSheetState
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import kotlinx.coroutines.launch

@Composable
public fun SharedTransitionScope.SettingsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    topBarBoundsKey: String,
    titleElementKey: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:Settings") {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    SettingsScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        topBarBoundsKey = topBarBoundsKey,
        titleElementKey = titleElementKey,
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        eventSink = eventSink,
        modifier = modifier,
    )
}

@Composable
internal fun SharedTransitionScope.SettingsScreen(
    topBarBoundsKey: String,
    titleElementKey: String,
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
                    state = uiState,
                    eventSink = eventSink,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentUi(
    state: SettingsUiState.Content,
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

            // TODO inject source code url
            SourceCodeTile(sourceCodeUrl = "https://github.com/ReactiveCircus/kstreamlined-mobile")

            Spacer(modifier = Modifier.height(8.dp))

            // TODO show licenses
            OpenSourceLicensesTile(onClick = {})

            Spacer(modifier = Modifier.height(8.dp))

            // TODO inject version
            VersionTile(version = "android-0.3.0 (4c52de9)")

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
