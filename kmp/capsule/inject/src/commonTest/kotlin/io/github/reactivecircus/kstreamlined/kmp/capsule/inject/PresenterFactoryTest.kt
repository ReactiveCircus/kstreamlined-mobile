package io.github.reactivecircus.kstreamlined.kmp.capsule.inject

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PresenterFactoryTest {
    private val moleculeContext = MoleculeContext(
        coroutineContext = StandardTestDispatcher(),
        recompositionMode = RecompositionMode.ContextClock,
    )

    @Test
    fun `create returns presenter from provider map`() {
        val presenter = FakePresenter(moleculeContext)
        val factory = testFactory(
            presenterProviders = mapOf(FakePresenter::class to { presenter }),
        )
        assertEquals(presenter, factory.create(FakePresenter::class))
    }

    @Test
    fun `create throws for unknown presenter class`() {
        val factory = testFactory()
        assertFailsWith<IllegalArgumentException> {
            factory.create(FakePresenter::class)
        }
    }

    @Test
    fun `createAssistedFactory returns factory from provider map`() {
        val assistedFactory = FakePresenter.Factory { FakePresenter(moleculeContext) }
        val factory = testFactory(
            assistedFactoryProviders = mapOf(FakePresenter.Factory::class to { assistedFactory }),
        )
        assertEquals(assistedFactory, factory.createAssistedFactory(FakePresenter.Factory::class)())
    }

    @Test
    fun `createAssistedFactory throws for unknown factory class`() {
        val factory = testFactory()
        assertFailsWith<IllegalStateException> {
            factory.createAssistedFactory(FakePresenter.Factory::class)
        }
    }
}

private fun testFactory(
    presenterProviders: Map<KClass<out Presenter<*, *>>, () -> Presenter<*, *>> = emptyMap(),
    assistedFactoryProviders: Map<KClass<out PresenterAssistedFactory>, () -> PresenterAssistedFactory> = emptyMap(),
) = object : PresenterFactory() {
    override val presenterProviders = presenterProviders
    override val assistedFactoryProviders = assistedFactoryProviders
}

private class FakePresenter(
    moleculeContext: MoleculeContext,
) : Presenter<Nothing, Unit>(moleculeContext) {
    @Composable
    override fun present() = Unit

    fun interface Factory : PresenterAssistedFactory {
        fun create(id: String): FakePresenter
    }
}
