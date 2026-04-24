package io.github.reactivecircus.kstreamlined.kmp.capsule.testing

import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import kotlinx.coroutines.test.TestDispatcher
import kotlin.coroutines.CoroutineContext

public fun TestDispatcher.asMoleculeContext(): MoleculeContext = object : MoleculeContext {
    override val coroutineContext: CoroutineContext
        get() = this@asMoleculeContext
    override val recompositionMode: RecompositionMode
        get() = RecompositionMode.Immediate
}
