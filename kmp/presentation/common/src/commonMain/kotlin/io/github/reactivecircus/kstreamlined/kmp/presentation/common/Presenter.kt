package io.github.reactivecircus.kstreamlined.kmp.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

public abstract class Presenter<UiEvent : Any, UiState : Any>(
    private val scope: CoroutineScope,
    private val recompositionMode: RecompositionMode,
) {
    private val events = MutableSharedFlow<UiEvent>()

    public val eventSink: (UiEvent) -> Unit = { scope.launch { events.emit(it) } }

    public val states: StateFlow<UiState> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(recompositionMode) { present() }
    }

    @Composable
    protected abstract fun present(): UiState

    @Composable
    protected fun CollectEvent(block: suspend (UiEvent) -> Unit) {
        LaunchedEffect(events) { events.collect { block(it) } }
    }
}
