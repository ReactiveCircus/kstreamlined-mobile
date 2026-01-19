@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class RouteBindingSymbols private constructor(
    val routeBindingAnnotation: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            routeBindingAnnotationId: ClassId,
        ): RouteBindingSymbols? {
            val routeBindingAnnotation = pluginContext.finderForBuiltins().findClass(routeBindingAnnotationId)
            if (routeBindingAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$routeBindingAnnotation>.",
                )
                return null
            }
            return RouteBindingSymbols(routeBindingAnnotation)
        }
    }
}
