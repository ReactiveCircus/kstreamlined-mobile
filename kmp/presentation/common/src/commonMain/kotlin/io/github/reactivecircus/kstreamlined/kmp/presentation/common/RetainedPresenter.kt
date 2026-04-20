package io.github.reactivecircus.kstreamlined.kmp.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.retain.RetainObserver
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain.Retainable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

public abstract class RetainedPresenter<UiEvent : Any, UiState : Any>(
    coroutineContext: CoroutineContext,
    private val recompositionMode: RecompositionMode,
) : Retainable, RetainObserver {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + coroutineContext)

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

    final override fun onRetained(): Unit = Unit
    final override fun onEnteredComposition(): Unit = Unit
    final override fun onExitedComposition(): Unit = Unit
    final override fun onRetired() {
        clear()
    }
    final override fun onUnused() {
        clear()
    }

    private fun clear() {
        scope.cancel()
        onCleared()
    }

    protected open fun onCleared() {}
}
