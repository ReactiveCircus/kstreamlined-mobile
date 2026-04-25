package io.github.reactivecircus.kstreamlined.kmp.capsule.testing

import app.cash.molecule.RecompositionMode
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.MoleculeContext
import kotlinx.coroutines.test.TestDispatcher

public fun TestDispatcher.asMoleculeContext(): MoleculeContext = MoleculeContext(
    coroutineContext = this,
    recompositionMode = RecompositionMode.Immediate,
)
