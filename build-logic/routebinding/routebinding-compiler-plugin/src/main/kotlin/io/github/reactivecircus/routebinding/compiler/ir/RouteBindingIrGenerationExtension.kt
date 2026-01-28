package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation

internal class RouteBindingIrGenerationExtension(
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val routeBindingFunctions = moduleFragment.files.asSequence()
            .flatMap { it.declarations }
            .filterIsInstance<IrSimpleFunction>()
            .filter { it.hasAnnotation(ClassIds.RouteBinding.Annotation) }
        if (routeBindingFunctions.none()) return

        val nav3Symbols = Nav3Symbols.create(
            pluginContext,
            messageCollector,
            entryProviderScopeClassId = ClassIds.Nav3.EntryProviderScope,
        )
        if (nav3Symbols == null) return

        moduleFragment.transform(
            RouteBindingClassTransformer(
                pluginContext,
                nav3Symbols,
                routeBindingFunctions,
            ),
            null,
        )
    }
}
