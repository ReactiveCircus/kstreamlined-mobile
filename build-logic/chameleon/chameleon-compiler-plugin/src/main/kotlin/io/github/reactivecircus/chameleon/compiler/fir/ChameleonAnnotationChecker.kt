package io.github.reactivecircus.chameleon.compiler.fir

import io.github.reactivecircus.chameleon.compiler.Chameleon
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirClassChecker
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.name.ClassId

internal object ChameleonAnnotationChecker : FirClassChecker(MppCheckerKind.Common) {
    context(context: CheckerContext, reporter: DiagnosticReporter)
    override fun check(declaration: FirClass) {
        if (!declaration.hasAnnotation(Chameleon.ANNOTATION_ID, context.session)) return

        if (!declaration.hasAnnotation(BurstAnnotationId, context.session)) {
            reporter.reportOn(declaration.source, ChameleonDiagnostics.MISSING_BURST_ANNOTATION)
        }
    }
}

private val BurstAnnotationId = ClassId.fromString("app/cash/burst/Burst")
