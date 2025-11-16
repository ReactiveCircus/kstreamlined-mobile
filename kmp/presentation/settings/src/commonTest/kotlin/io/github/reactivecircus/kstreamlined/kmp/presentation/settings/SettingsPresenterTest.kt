package io.github.reactivecircus.kstreamlined.kmp.presentation.settings

import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
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
        scope = testScope.backgroundScope,
        recompositionMode = RecompositionMode.Immediate,
    )

    @Test
    fun `presenter emits Loading state followed by Content state with current appSettings when initialized`() = testScope.runTest {
        presenter.states.test {
            assertEquals(SettingsUiState.Loading, awaitItem())

            assertEquals(SettingsUiState.Content(AppSettings.Default), awaitItem())
        }
    }

    @Test
    fun `presenter emits Content state with new appSettings when SelectTheme event is dispatched`() {
        testScope.runTest {
            presenter.states.test {
                assertEquals(SettingsUiState.Loading, awaitItem())

                assertEquals(SettingsUiState.Content(AppSettings.Default), awaitItem())

                presenter.eventSink(SettingsUiEvent.SelectTheme(AppSettings.Theme.Light))

                assertEquals(
                    SettingsUiState.Content(
                        AppSettings.Default.copy(theme = AppSettings.Theme.Light),
                    ),
                    awaitItem(),
                )

                presenter.eventSink(SettingsUiEvent.SelectTheme(AppSettings.Theme.Dark))

                assertEquals(
                    SettingsUiState.Content(
                        AppSettings.Default.copy(theme = AppSettings.Theme.Dark),
                    ),
                    awaitItem(),
                )
            }
        }
    }
}
