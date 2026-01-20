package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.ComposeSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class RouteBindingIrGenerationExtension(
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val routeBindingSymbols = RouteBindingSymbols.create(
            pluginContext,
            messageCollector,
            routeBindingAnnotationId = ClassIds.RouteBinding.Annotation,
            navEntryInstallerClassId = ClassIds.RouteBinding.NavEntryInstaller,
        )
        val composeSymbols = ComposeSymbols.create(
            pluginContext,
            messageCollector,
            composableAnnotationId = ClassIds.Compose.Composable,
            sharedTransitionScopeClassId = ClassIds.Compose.SharedTransitionScope,
        )
        val nav3Symbols = Nav3Symbols.create(
            pluginContext,
            messageCollector,
            navKeyClassId = ClassIds.Nav3.NavKey,
            navBackStackClassId = ClassIds.Nav3.NavBackStack,
            entryProviderScopeClassId = ClassIds.Nav3.EntryProviderScope,
        )
        val metroSymbols = MetroSymbols.create(
            pluginContext,
            messageCollector,
            appScopeAnnotationId = ClassIds.Metro.AppScope,
            contributesIntoSetAnnotationId = ClassIds.Metro.ContributesIntoSet,
        )

        @Suppress("ComplexCondition")
        if (routeBindingSymbols == null || composeSymbols == null || nav3Symbols == null || metroSymbols == null) return

        moduleFragment.transform(
            RouteBindingFunctionTransformer(
                pluginContext,
                routeBindingSymbols,
                composeSymbols,
                nav3Symbols,
                metroSymbols,
            ),
            null,
        )
    }
}
