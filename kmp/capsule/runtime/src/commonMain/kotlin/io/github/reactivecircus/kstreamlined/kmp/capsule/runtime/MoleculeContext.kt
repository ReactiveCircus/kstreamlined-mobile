package io.github.reactivecircus.kstreamlined.kmp.capsule.runtime

import app.cash.molecule.RecompositionMode
import kotlin.coroutines.CoroutineContext

public class MoleculeContext(
    public val coroutineContext: CoroutineContext,
    public val recompositionMode: RecompositionMode,
)
