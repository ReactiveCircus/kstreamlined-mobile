package io.github.reactivecircus.kstreamlined.kmp.capsule.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.turbine.test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PresenterTest {
    private val testDispatcher = StandardTestDispatcher()
    private val moleculeContext = MoleculeContext(testDispatcher, RecompositionMode.Immediate)

    @Test
    fun `states emits values when present function is composed`() = runTest(testDispatcher) {
        val presenter = TestPresenter(moleculeContext)
        presenter.states.test {
            assertEquals(TestState("initial"), awaitItem())
        }
    }

    @Test
    fun `states emits updated values when events are sent`() = runTest(testDispatcher) {
        val presenter = TestPresenter(moleculeContext)
        presenter.states.test {
            assertEquals(TestState("initial"), awaitItem())
            presenter.eventSink(TestEvent.Update("updated"))
            assertEquals(TestState("updated"), awaitItem())
        }
    }

    @Test
    fun `onRetired cancels CoroutineScope and calls onCleared`() = runTest(testDispatcher) {
        val presenter = TestPresenter(moleculeContext)
        presenter.states.test {
            assertEquals(TestState("initial"), awaitItem())
            presenter.onRetired()
            assertTrue(presenter.cleared)
            presenter.eventSink(TestEvent.Update("ignored"))
            expectNoEvents()
        }
    }

    @Test
    fun `onUnused cancels CoroutineScope and calls onCleared`() = runTest(testDispatcher) {
        val presenter = TestPresenter(moleculeContext)
        presenter.states.test {
            assertEquals(TestState("initial"), awaitItem())
            presenter.onUnused()
            assertTrue(presenter.cleared)
            presenter.eventSink(TestEvent.Update("ignored"))
            expectNoEvents()
        }
    }
}

private data class TestState(val value: String)

private sealed interface TestEvent {
    data class Update(val value: String) : TestEvent
}

private class TestPresenter(
    moleculeContext: MoleculeContext,
) : Presenter<TestEvent, TestState>(moleculeContext) {
    var cleared = false
        private set

    @Composable
    override fun present(): TestState {
        var value by remember { mutableStateOf("initial") }
        CollectEvent { event ->
            when (event) {
                is TestEvent.Update -> value = event.value
            }
        }
        return TestState(value)
    }

    override fun onCleared() {
        cleared = true
    }
}
