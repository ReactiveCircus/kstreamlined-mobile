package io.github.reactivecircus.kstreamlined.kmp.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.flow.StateFlow

public abstract class Presenter<UiState, UiEvent> {
    public abstract val uiState: StateFlow<UiState>
    public abstract val eventSink: (UiEvent) -> Unit
}

@Composable
public fun test() {
    rememberCoroutineScope().launchMolecule(RecompositionMode.ContextClock) {

    }
}
