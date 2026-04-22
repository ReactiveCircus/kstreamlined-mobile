package io.github.reactivecircus.kstreamlined.kmp.capsule.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.retain.RetainObserver
import app.cash.molecule.launchMolecule
import dev.zacsweers.metro.DefaultBinding
import dev.zacsweers.metro.ExperimentalMetroApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMetroApi::class)
@DefaultBinding<Presenter<*, *>>
public abstract class Presenter<UiEvent : Any, UiState : Any>(
    moleculeContext: MoleculeContext,
) : RetainObserver {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + moleculeContext.coroutineContext)

    private val events = MutableSharedFlow<UiEvent>()

    public val eventSink: (UiEvent) -> Unit = { scope.launch { events.emit(it) } }

    public val states: StateFlow<UiState> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(moleculeContext.recompositionMode) { present() }
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
