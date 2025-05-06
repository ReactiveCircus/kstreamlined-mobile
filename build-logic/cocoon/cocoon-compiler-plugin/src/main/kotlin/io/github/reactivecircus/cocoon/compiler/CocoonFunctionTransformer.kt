package io.github.reactivecircus.cocoon.compiler

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

internal class CocoonFunctionTransformer(
    private val pluginContext: IrPluginContext,
    private val messageCollector: MessageCollector,
    private val annotationName: ClassId,
    private val wrappingFunctionName: CallableId,
) : IrElementTransformerVoidWithContext() {

}
