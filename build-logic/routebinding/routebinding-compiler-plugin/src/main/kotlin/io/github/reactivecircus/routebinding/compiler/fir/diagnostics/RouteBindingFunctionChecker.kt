package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.effectiveVisibility
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.fir.resolve.getContainingClass

// TODO move to FirSimpleFunctionChecker / FirNamedFunction once Studio bundles Kotlin 2.3.20+.
internal object RouteBindingFunctionChecker : FirFunctionChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirFunction) {
        if (!declaration.hasAnnotation(ClassIds.RouteBinding.Annotation, context.session)) return

        if (declaration.getContainingClass() != null) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_MUST_BE_TOP_LEVEL,
                declaration.nameOrSpecialName.asString(),
            )
        }

        if (!declaration.hasAnnotation(ClassIds.Compose.Composable, context.session)) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_MUST_BE_COMPOSABLE,
                declaration.nameOrSpecialName.asString(),
            )
        }

        val visibility = declaration.effectiveVisibility.toVisibility()

        if (visibility == Visibilities.Private) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_CANNOT_BE_PRIVATE,
                declaration.nameOrSpecialName.asString(),
            )
        }
        if (visibility.isPublicAPI) {
            reporter.reportOn(
                declaration.source,
                RouteBindingDiagnostics.FUNCTION_CAN_BE_INTERNAL,
                declaration.nameOrSpecialName.asString(),
            )
        }
    }
}
