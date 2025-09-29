package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

internal class ChameleonIrGenerationExtension(
    private val chameleonAnnotationId: ClassId,
    private val snapshotFunctionId: CallableId,
    private val themeVariantEnumId: ClassId,
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    @Suppress("ReturnCount")
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        if (pluginContext.referenceClass(chameleonAnnotationId) == null) {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Could not find annotation class <$chameleonAnnotationId>.",
            )
            return
        }
        if (pluginContext.referenceFunctions(snapshotFunctionId).isEmpty()) {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Could not find snapshot function <$snapshotFunctionId>.",
            )
            return
        }
        if (pluginContext.referenceClass(themeVariantEnumId) == null) {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Could not find theme variant enum class <$themeVariantEnumId>.",
            )
            return
        }
        moduleFragment.transform(
            ChameleonFunctionTransformer(
                pluginContext,
                messageCollector,
                chameleonAnnotationId,
                snapshotFunctionId,
                themeVariantEnumId,
            ),
            null,
        )
    }
}
