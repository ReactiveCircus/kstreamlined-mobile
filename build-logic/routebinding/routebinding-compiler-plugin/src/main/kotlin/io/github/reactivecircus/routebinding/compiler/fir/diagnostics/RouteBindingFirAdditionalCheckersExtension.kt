package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirNamedFunctionChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

internal class RouteBindingFirAdditionalCheckersExtension(
    session: FirSession,
) : FirAdditionalCheckersExtension(session) {
    override val declarationCheckers: DeclarationCheckers = object : DeclarationCheckers() {
        override val namedFunctionCheckers: Set<FirNamedFunctionChecker>
            get() = setOf(RouteBindingFunctionChecker)
    }
}
