@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ir.symbols.ComposeSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation

internal class RouteBindingFunctionTransformer(
    private val pluginContext: IrPluginContext,
    private val routeBindingSymbols: RouteBindingSymbols,
    private val composeSymbols: ComposeSymbols,
    private val nav3Symbols: Nav3Symbols,
    private val metroSymbols: MetroSymbols,
) : IrElementTransformerVoidWithContext() {
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        // skip transform if function isn't annotated with @RouteBinding
        if (!declaration.hasAnnotation(routeBindingSymbols.routeBindingAnnotation)) {
            return super.visitFunctionNew(declaration)
        }

        return super.visitFunctionNew(declaration)
    }
}
