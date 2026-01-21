@file:Suppress("MaxLineLength", "ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class Nav3Symbols private constructor(
    val navKeyInterface: IrClassSymbol,
    val navBackStackClass: IrClassSymbol,
    val entryProviderScopeClass: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            navKeyClassId: ClassId,
            navBackStackClassId: ClassId,
            entryProviderScopeClassId: ClassId,
        ): Nav3Symbols? {
            val navKeyInterface = pluginContext.finderForBuiltins().findClass(navKeyClassId)
            if (navKeyInterface == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find interface <$navKeyClassId>.",
                )
                return null
            }
            val navBackStackClass = pluginContext.finderForBuiltins().findClass(navBackStackClassId)
            if (navBackStackClass == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find class <$navBackStackClassId>.",
                )
                return null
            }
            val entryProviderScopeClass = pluginContext.finderForBuiltins().findClass(entryProviderScopeClassId)
            if (entryProviderScopeClass == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find class <$entryProviderScopeClassId>.",
                )
                return null
            }
            return Nav3Symbols(
                navKeyInterface = navKeyInterface,
                navBackStackClass = navBackStackClass,
                entryProviderScopeClass = entryProviderScopeClass,
            )
        }
    }
}
