package io.github.reactivecircus.chameleon.compiler.ir

import io.github.reactivecircus.chameleon.compiler.ClassIds
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
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
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val chameleonSymbols = ChameleonSymbols.create(
            pluginContext,
            messageCollector,
            chameleonAnnotationId,
            snapshotFunctionId,
            themeVariantEnumId,
        )
        val burstSymbols = BurstSymbols.create(
            pluginContext,
            messageCollector,
            ClassIds.Burst.Annotation,
        )
        if (chameleonSymbols == null || burstSymbols == null) return
        moduleFragment.transform(
            ChameleonClassTransformer(
                pluginContext,
                chameleonSymbols,
                burstSymbols,
            ),
            null,
        )
    }
}
