package io.github.reactivecircus.routebinding.compiler.fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension

internal class RouteBindingClassGenerator(session: FirSession) : FirDeclarationGenerationExtension(session)
