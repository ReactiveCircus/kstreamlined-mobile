@file:Suppress("PropertyName")

package io.github.reactivecircus.routebinding.compiler.fir.diagnostics

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticsContainer
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.DEFAULT
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.FUNCTION_TYPE_RECEIVER
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.NAME_IDENTIFIER
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies.VISIBILITY_MODIFIER
import org.jetbrains.kotlin.diagnostics.error1
import org.jetbrains.kotlin.diagnostics.error2
import org.jetbrains.kotlin.diagnostics.error3
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.CommonRenderers
import org.jetbrains.kotlin.diagnostics.warning1
import org.jetbrains.kotlin.psi.KtElement

internal object RouteBindingDiagnostics : KtDiagnosticsContainer() {
    val FUNCTION_MUST_BE_TOP_LEVEL by error1<KtElement, String>(NAME_IDENTIFIER)

    val FUNCTION_MUST_BE_COMPOSABLE by error1<KtElement, String>(NAME_IDENTIFIER)
    val FUNCTION_CANNOT_BE_PRIVATE by error1<KtElement, String>(VISIBILITY_MODIFIER)
    val FUNCTION_CAN_BE_INTERNAL by warning1<KtElement, String>(VISIBILITY_MODIFIER)

    val UNSUPPORTED_RECEIVER_TYPE by error2<KtElement, String, String>(FUNCTION_TYPE_RECEIVER)
    val UNSUPPORTED_VALUE_PARAMETER_TYPE by error3<KtElement, String, String, String>(DEFAULT)
    val UNSUPPORTED_CONTEXT_PARAMETER_TYPE by error3<KtElement, String, String, String>(DEFAULT)

    val DUPLICATE_PARAMETER_TYPE by error2<KtElement, String, String>(DEFAULT)

    override fun getRendererFactory(): BaseDiagnosticRendererFactory {
        return RouteBindingRendererFactory
    }
}

private object RouteBindingRendererFactory : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap by KtDiagnosticFactoryToRendererMap("RouteBinding") {
        it.put(
            RouteBindingDiagnostics.FUNCTION_MUST_BE_TOP_LEVEL,
            "`@RouteBinding` function `{0}` must be top level function.",
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.FUNCTION_MUST_BE_COMPOSABLE,
            "`@RouteBinding` function `{0}` must be a `@Composable` function.",
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.FUNCTION_CANNOT_BE_PRIVATE,
            "`@RouteBinding` function `{0}` cannot be private." +
                " Change the visibility of the function to internal instead.",
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.FUNCTION_CAN_BE_INTERNAL,
            "`@RouteBinding` function `{0}` does not need to be visible to other modules." +
                " Consider changing its visibility to internal.",
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.UNSUPPORTED_RECEIVER_TYPE,
            "Extension receiver of `@RouteBinding` function `{0}` has unsupported type `{1}`. " +
                getSupportedTypesSuffix(isValueParameter = false),
            CommonRenderers.STRING,
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.UNSUPPORTED_VALUE_PARAMETER_TYPE,
            "Parameter `{1}` of `@RouteBinding` function `{0}` has unsupported type `{2}`. " +
                getSupportedTypesSuffix(isValueParameter = true),
            CommonRenderers.STRING,
            CommonRenderers.STRING,
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.UNSUPPORTED_CONTEXT_PARAMETER_TYPE,
            "Context parameter `{1}` of `@RouteBinding` function `{0}` has unsupported type `{2}`. " +
                getSupportedTypesSuffix(isValueParameter = false),
            CommonRenderers.STRING,
            CommonRenderers.STRING,
            CommonRenderers.STRING,
        )
        it.put(
            RouteBindingDiagnostics.DUPLICATE_PARAMETER_TYPE,
            "`@RouteBinding` function `{0}` has multiple parameters of type `{1}`." +
                " Only one parameter per supported type is allowed.",
            CommonRenderers.STRING,
            CommonRenderers.STRING,
        )
    }
}

private fun getSupportedTypesSuffix(isValueParameter: Boolean) = buildString {
    append(
        """
        |Supported types are:
        | • `SharedTransitionScope`
        | • `NavBackStack<NavKey>`
        | • subtype of `NavKey`
        """.trimMargin(),
    )
    if (isValueParameter) {
        append("\n • anything with a default value")
    }
}
