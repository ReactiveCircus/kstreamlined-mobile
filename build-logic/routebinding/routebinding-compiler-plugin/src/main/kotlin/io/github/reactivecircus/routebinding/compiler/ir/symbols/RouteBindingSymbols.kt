package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class RouteBindingSymbols private constructor(
    val navEntryInstallInterface: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            navEntryInstallerClassId: ClassId,
        ): RouteBindingSymbols? {
            val navEntryInstallInterface = pluginContext.finderForBuiltins().findClass(navEntryInstallerClassId)
            if (navEntryInstallInterface == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find interface <$navEntryInstallerClassId>.",
                )
                return null
            }

            return RouteBindingSymbols(
                navEntryInstallInterface = navEntryInstallInterface,
            )
        }
    }
}
