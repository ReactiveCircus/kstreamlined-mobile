package io.github.reactivecircus.cocoon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

internal class CocoonIrGenerationExtension(
    private val annotationName: ClassId,
    private val wrappingFunctionName: CallableId,
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        if (pluginContext.referenceClass(annotationName) == null) {
            messageCollector.report(CompilerMessageSeverity.ERROR, "Could not find annotation class <$annotationName>.")
            return
        }
        if (pluginContext.referenceFunctions(wrappingFunctionName).isEmpty()) {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Could not find wrapping function <$wrappingFunctionName>.",
            )
            return
        }
        moduleFragment.transform(
            CocoonFunctionTransformer(pluginContext, messageCollector, annotationName, wrappingFunctionName),
            null,
        )
    }
}
