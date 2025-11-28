package io.github.reactivecircus.kstreamlined.android.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.AutoSyncInterval
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.SettingsUiState
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class SettingsScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SettingsScreen() {
        snapshotTester.snapshot {
            HomeSettingsSnapshot(
                uiState = SettingsUiState.Content(
                    theme = AppSettings.Default.theme,
                    autoSyncEnabled = AppSettings.Default.autoSync,
                    autoSyncInterval = AutoSyncInterval.Every6Hours,
                ),
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun HomeSettingsSnapshot(uiState: SettingsUiState) {
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = true,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                SettingsScreen(
                    topBarBoundsKey = "",
                    titleElementKey = "",
                    onNavigateUp = {},
                    uiState = uiState,
                    eventSink = {},
                )
            }
        }
    }
}
