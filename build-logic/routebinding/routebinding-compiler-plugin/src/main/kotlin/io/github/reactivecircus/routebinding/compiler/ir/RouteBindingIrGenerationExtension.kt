package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
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
        moduleFragment.files
            .flatMap { it.declarations }
            .filterIsInstance<IrSimpleFunction>()
            .filter { it.hasAnnotation(ClassIds.RouteBinding.Annotation) }
            .let { routeBindingFunctions ->
                if (routeBindingFunctions.isEmpty()) return
                val metroSymbols = MetroSymbols.create(
                    pluginContext,
                    messageCollector,
                    contributesIntoSetClassId = ClassIds.Metro.ContributesIntoSet,
                    appScopeClassId = ClassIds.Metro.AppScope,
                )
                val nav3Symbols = Nav3Symbols.create(
                    pluginContext,
                    messageCollector,
                    entryProviderScopeClassId = ClassIds.Nav3.EntryProviderScope,
                )
                val routeBindingSymbols = RouteBindingSymbols.create(
                    pluginContext,
                    messageCollector,
                    navEntryInstallerClassId = ClassIds.RouteBinding.NavEntryInstaller,
                )
                if (metroSymbols == null || nav3Symbols == null || routeBindingSymbols == null) return

                // Generate the NavEntryInstaller class for each `@RouteBinding`-annotated source function.
                val generator = RouteBindingClassGenerator(
                    pluginContext,
                    metroSymbols,
                    nav3Symbols,
                    routeBindingSymbols,
                )
                for (sourceFunction in routeBindingFunctions) {
                    generator.generate(sourceFunction)
                }
            }
    }
}
