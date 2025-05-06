package io.github.reactivecircus.cocoon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

public class CocoonCompilerPluginRegistrar : CompilerPluginRegistrar() {

    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val annotationString = requireNotNull(
            configuration.get(CocoonCommandLineProcessor.CompilerOptions.Annotation)
        )
        val annotationClassId = annotationString.toClassId()

        val wrappingFunctionString = requireNotNull(
            configuration.get(CocoonCommandLineProcessor.CompilerOptions.WrappingFunction)
        )
        val wrappingFunctionCallableId = wrappingFunctionString.toCallableId()

        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE
        )

        IrGenerationExtension.registerExtension(
            extension = CocoonIrGenerationExtension(
                annotationName = annotationClassId,
                wrappingFunctionName = wrappingFunctionCallableId,
                messageCollector = messageCollector,
            )
        )
    }
}
