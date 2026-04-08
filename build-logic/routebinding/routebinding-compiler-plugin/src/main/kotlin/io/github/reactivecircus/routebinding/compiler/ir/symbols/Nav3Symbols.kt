package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class Nav3Symbols private constructor(
    val entryFunction: IrSimpleFunctionSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            entryProviderScopeClassId: ClassId,
        ): Nav3Symbols? {
            val entryFunctionCallableId = CallableId(
                classId = entryProviderScopeClassId,
                callableName = Name.identifier("entry"),
            )
            val entryFunction = pluginContext.finderForBuiltins()
                .findFunctions(entryFunctionCallableId)
                .singleOrNull { function ->
                    val params = function.owner.parameters
                    params.count { it.defaultValue == null } == 2 &&
                        params.indexOfLast { it.defaultValue == null } == params.size - 1
                }
            if (entryFunction == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find function <$entryFunctionCallableId>.",
                )
                return null
            }
            return Nav3Symbols(entryFunction = entryFunction)
        }
    }
}
