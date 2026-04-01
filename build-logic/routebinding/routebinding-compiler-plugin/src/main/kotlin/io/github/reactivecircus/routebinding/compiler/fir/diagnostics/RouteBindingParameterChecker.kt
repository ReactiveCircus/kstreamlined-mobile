package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.context.findClosest
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirValueParameterChecker
import org.jetbrains.kotlin.fir.declarations.FirValueParameter
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.symbols.impl.FirFunctionSymbol
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.renderReadable

internal object RouteBindingParameterChecker : FirValueParameterChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirValueParameter) {
        val parentFunction = context.findClosest<FirFunctionSymbol<*>>() ?: return
        if (!parentFunction.hasAnnotation(ClassIds.RouteBinding.Annotation, context.session)) return

        val paramType = declaration.returnTypeRef.coneType
        val functionName = parentFunction.name.asString()
        val paramName = declaration.name.asString()

        val isContextParam = parentFunction.contextParameterSymbols.any { it == declaration.symbol }

        if (isContextParam) {
            if (!paramType.isSupportedRouteBindingType(context.session)) {
                reporter.reportOn(
                    declaration.source,
                    RouteBindingDiagnostics.UNSUPPORTED_CONTEXT_PARAMETER_TYPE,
                    functionName,
                    paramName,
                    paramType.renderReadable(),
                )
            }
        } else {
            if (declaration.defaultValue == null && !paramType.isSupportedRouteBindingType(context.session)) {
                reporter.reportOn(
                    declaration.source,
                    RouteBindingDiagnostics.UNSUPPORTED_VALUE_PARAMETER_TYPE,
                    functionName,
                    paramName,
                    paramType.renderReadable(),
                )
            }
        }
    }
}
