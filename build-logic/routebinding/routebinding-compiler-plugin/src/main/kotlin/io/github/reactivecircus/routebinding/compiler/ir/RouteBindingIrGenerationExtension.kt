package io.github.reactivecircus.routebinding.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.name.ClassId

internal class RouteBindingIrGenerationExtension(
    private val routeBindingAnnotationId: ClassId,
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val routeBindingSymbols = RouteBindingSymbols.create(
            pluginContext,
            messageCollector,
            routeBindingAnnotationId,
        )
        if (routeBindingSymbols == null) return
        moduleFragment.transform(
            RouteBindingFunctionTransformer(
                pluginContext,
                routeBindingSymbols,
            ),
            null,
        )
    }
}
