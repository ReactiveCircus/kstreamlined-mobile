@file:Suppress("MaxLineLength", "ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId

internal class ComposeSymbols private constructor(
    val composableAnnotation: IrClassSymbol,
    val sharedTransitionScopeInterface: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            composableAnnotationId: ClassId,
            sharedTransitionScopeClassId: ClassId,
        ): ComposeSymbols? {
            val composableAnnotation = pluginContext.finderForBuiltins().findClass(composableAnnotationId)
            if (composableAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$composableAnnotation>.",
                )
                return null
            }
            val sharedTransitionScopeInterface = pluginContext.finderForBuiltins().findClass(
                sharedTransitionScopeClassId,
            )
            if (sharedTransitionScopeInterface == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find interface <$sharedTransitionScopeInterface>.",
                )
                return null
            }
            return ComposeSymbols(
                composableAnnotation = composableAnnotation,
                sharedTransitionScopeInterface = sharedTransitionScopeInterface,
            )
        }
    }
}
