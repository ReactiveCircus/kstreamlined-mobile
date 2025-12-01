package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import io.github.reactivecircus.kstreamlined.kmp.appinfo.AppInfo
import io.github.reactivecircus.kstreamlined.kmp.datastore.testing.createFakeDataStore
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsPresenterTest {
    private val settingsDataStore = createFakeDataStore()

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val presenter = SettingsPresenter(
        settingsDataSource = SettingsDataSource(settingsDataStore),
        appInfo = AppInfo(
            versionName = "1.0.0",
            sourceCodeUrl = "source-url",
        ),
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Loading state followed by Content state with current app settings when initialized`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(SettingsUiState.Loading, awaitItem())

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated app settings when SelectTheme event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(SettingsUiState.Loading, awaitItem())

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.SelectTheme(AppSettings.Theme.Light))

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Theme.Light,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.SelectTheme(AppSettings.Theme.Dark))

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Theme.Dark,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.from(
                            AppSettings.Default.autoSyncInterval,
                        ),
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated app settings when ToggleAutoSync event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(SettingsUiState.Loading, awaitItem())

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.ToggleAutoSync)

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = false,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.ToggleAutoSync)

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = true,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `presenter emits Content state with updated app settings when SelectSyncInterval event is dispatched`() =
        testScope.runTest {
            presenter.states.test {
                assertEquals(SettingsUiState.Loading, awaitItem())

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Default,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.SelectSyncInterval(AutoSyncInterval.Hourly))

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Hourly,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.SelectSyncInterval(AutoSyncInterval.Daily))

                assertEquals(
                    SettingsUiState.Content(
                        theme = AppSettings.Default.theme,
                        autoSyncEnabled = AppSettings.Default.autoSync,
                        autoSyncInterval = AutoSyncInterval.Daily,
                        versionName = "1.0.0",
                        sourceCodeUrl = "source-url",
                    ),
                    awaitItem(),
                )
            }
        }
}
