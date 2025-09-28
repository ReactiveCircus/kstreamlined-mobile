package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

public class ChameleonCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val annotationString = "io.github.reactivecircus.chameleon.runtime.Chameleon" // TODO move
        val annotationClassId = annotationString.toClassId()

        val snapshotFunctionString = requireNotNull(
            configuration.get(ChameleonCommandLineProcessor.CompilerOptions.SnapshotFunction),
        )
        val snapshotFunctionCallableId = snapshotFunctionString.toCallableId()

        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE,
        )

        IrGenerationExtension.registerExtension(
            extension = ChameleonIrGenerationExtension(
                annotationName = annotationClassId,
                snapshotFunctionName = snapshotFunctionCallableId,
                messageCollector = messageCollector,
            ),
        )
    }
}
