package io.github.reactivecircus.routebinding.compiler.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction

internal class RouteBindingFunctionTransformer(
    private val pluginContext: IrPluginContext,
    private val routeBindingSymbols: RouteBindingSymbols,
) : IrElementTransformerVoidWithContext() {
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        // TODO
        return super.visitFunctionNew(declaration)
    }
}
