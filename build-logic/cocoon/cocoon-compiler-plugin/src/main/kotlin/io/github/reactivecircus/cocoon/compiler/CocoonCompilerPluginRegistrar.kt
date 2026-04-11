package io.github.reactivecircus.cocoon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

public class CocoonCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val pluginId: String get() = "io.github.reactivecircus.cocoon.compiler"

    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val annotationString = requireNotNull(
            configuration[CocoonCommandLineProcessor.CompilerOptions.Annotation],
        )
        val annotationClassId = annotationString.toClassId()

        val wrappingFunctionString = requireNotNull(
            configuration[CocoonCommandLineProcessor.CompilerOptions.WrappingFunction],
        )
        val wrappingFunctionCallableId = wrappingFunctionString.toCallableId()

        val messageCollector = configuration[CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE]

        IrGenerationExtension.registerExtension(
            extension = CocoonIrGenerationExtension(
                annotationName = annotationClassId,
                wrappingFunctionName = wrappingFunctionCallableId,
                messageCollector = messageCollector,
            ),
        )
    }
}
