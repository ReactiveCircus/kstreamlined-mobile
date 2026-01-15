@file:Suppress("MaxLineLength", "ReturnCount")

package io.github.reactivecircus.chameleon.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.isEnumClass
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

internal class ChameleonSymbols private constructor(
    val chameleonAnnotation: IrClassSymbol,
    val snapshotFunction: IrSimpleFunctionSymbol,
    val themeVariantEnum: IrClassSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            chameleonAnnotationId: ClassId,
            snapshotFunctionId: CallableId,
            themeVariantEnumId: ClassId,
        ): ChameleonSymbols? {
            val chameleonAnnotation = pluginContext.finderForBuiltins().findClass(chameleonAnnotationId)
            if (chameleonAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$chameleonAnnotationId>.",
                )
                return null
            }

            val snapshotFunction = pluginContext.finderForBuiltins().findFunctions(snapshotFunctionId).singleOrNull()
            if (snapshotFunction == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find snapshot function <$snapshotFunctionId>.",
                )
                return null
            }

            val themeVariantEnum = pluginContext.finderForBuiltins().findClass(themeVariantEnumId)
            if (themeVariantEnum == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find theme variant enum class <$themeVariantEnumId>.",
                )
                return null
            }
            if (!themeVariantEnum.owner.isEnumClass) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "<$themeVariantEnumId> is not an enum.",
                )
                return null
            }
            if (snapshotFunction.owner.parameters.none { it.type.getClass()?.classId == themeVariantEnumId }) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Theme variant enum <$themeVariantEnumId> is not a parameter of snapshot function <$snapshotFunctionId>.",
                )
                return null
            }
            return ChameleonSymbols(chameleonAnnotation, snapshotFunction, themeVariantEnum)
        }
    }
}
