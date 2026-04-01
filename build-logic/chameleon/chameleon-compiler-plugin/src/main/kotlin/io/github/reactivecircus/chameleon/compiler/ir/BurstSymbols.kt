@file:Suppress("MaxLineLength")

package io.github.reactivecircus.chameleon.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class BurstSymbols private constructor(val burstAnnotation: IrClassSymbol) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            burstAnnotationId: ClassId,
        ): BurstSymbols? {
            val burstAnnotation = pluginContext.finderForBuiltins().findClass(burstAnnotationId)
            if (burstAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$burstAnnotationId>. Please make sure the `app.cash.burst` plugin has been applied to the project.",
                )
                return null
            }
            return BurstSymbols(burstAnnotation)
        }
    }
}
