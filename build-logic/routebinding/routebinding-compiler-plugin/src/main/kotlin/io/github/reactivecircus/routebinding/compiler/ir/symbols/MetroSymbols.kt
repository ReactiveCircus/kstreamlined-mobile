package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class MetroSymbols private constructor(
    val contributesIntoSetClass: IrClassSymbol,
    val appScopeClass: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            contributesIntoSetClassId: ClassId,
            appScopeClassId: ClassId,
        ): MetroSymbols? {
            val contributesIntoSetClass = pluginContext.finderForBuiltins().findClass(contributesIntoSetClassId)
            if (contributesIntoSetClass == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find class <$contributesIntoSetClassId>.",
                )
                return null
            }

            val appScopeClass = pluginContext.finderForBuiltins().findClass(appScopeClassId)
            if (appScopeClass == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find class <$appScopeClassId>.",
                )
                return null
            }

            return MetroSymbols(
                contributesIntoSetClass = contributesIntoSetClass,
                appScopeClass = appScopeClass,
            )
        }
    }
}
