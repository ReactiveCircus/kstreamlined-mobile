package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

internal class ChameleonIrGenerationExtension(
    private val annotationName: ClassId,
    private val snapshotFunctionName: CallableId,
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        if (pluginContext.referenceClass(annotationName) == null) return
//        if (pluginContext.referenceFunctions(snapshotFunctionName).isEmpty()) {
//            messageCollector.report(
//                CompilerMessageSeverity.ERROR,
//                "Could not find snapshot function <$snapshotFunctionName>.",
//            )
//            return
//        }
        moduleFragment.transform(
            ChameleonFunctionTransformer(pluginContext, messageCollector, annotationName, snapshotFunctionName),
            null,
        )
    }
}
