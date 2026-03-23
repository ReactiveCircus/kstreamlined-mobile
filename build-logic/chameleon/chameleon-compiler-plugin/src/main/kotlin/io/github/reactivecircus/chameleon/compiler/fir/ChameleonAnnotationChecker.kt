package io.github.reactivecircus.chameleon.compiler.fir

import io.github.reactivecircus.chameleon.compiler.ClassIds
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirClassChecker
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassId

internal object ChameleonAnnotationChecker : FirClassChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirClass) {
        if (!declaration.hasAnnotation(ClassIds.Chameleon.Annotation, context.session)) return

        val burstAnnotation = declaration.annotations.firstOrNull {
            it.toAnnotationClassId(context.session) == ClassIds.Burst.Annotation
        }
        if (burstAnnotation != null) {
            reporter.reportOn(burstAnnotation.source, ChameleonDiagnostics.REDUNDANT_BURST_ANNOTATION)
        }
    }
}
