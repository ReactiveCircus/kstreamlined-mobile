@file:Suppress("MaxLineLength", "ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class MetroSymbols private constructor(
    val appScopeAnnotation: IrClassSymbol,
    val contributesIntoSetAnnotation: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            appScopeAnnotationId: ClassId,
            contributesIntoSetAnnotationId: ClassId,
        ): MetroSymbols? {
            val appScopeAnnotation = pluginContext.finderForBuiltins().findClass(appScopeAnnotationId)
            if (appScopeAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$appScopeAnnotationId>.",
                )
                return null
            }
            val contributesIntoSetAnnotation = pluginContext.finderForBuiltins().findClass(
                contributesIntoSetAnnotationId,
            )
            if (contributesIntoSetAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$contributesIntoSetAnnotationId>.",
                )
                return null
            }
            return MetroSymbols(
                appScopeAnnotation = appScopeAnnotation,
                contributesIntoSetAnnotation = contributesIntoSetAnnotation,
            )
        }
    }
}
