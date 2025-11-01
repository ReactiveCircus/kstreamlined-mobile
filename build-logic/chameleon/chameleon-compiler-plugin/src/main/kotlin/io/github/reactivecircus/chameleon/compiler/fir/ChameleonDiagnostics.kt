package io.github.reactivecircus.chameleon.compiler.fir

import io.github.reactivecircus.chameleon.compiler.fir.ChameleonDiagnostics.MISSING_BURST_ANNOTATION
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticsContainer
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.NAME_IDENTIFIER
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtElement

internal object ChameleonDiagnostics : KtDiagnosticsContainer() {
    val MISSING_BURST_ANNOTATION by error0<KtElement>(NAME_IDENTIFIER)

    override fun getRendererFactory(): BaseDiagnosticRendererFactory {
        return ChameleonErrors
    }
}

private object ChameleonErrors : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap by KtDiagnosticFactoryToRendererMap("Chameleon") {
        it.put(MISSING_BURST_ANNOTATION, "Classes annotated with `@Chameleon` must also be annotated with `@Burst`.")
    }
}
