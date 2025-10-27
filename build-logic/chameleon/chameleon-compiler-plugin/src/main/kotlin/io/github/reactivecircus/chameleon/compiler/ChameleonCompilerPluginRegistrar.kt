package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.name.ClassId

public class ChameleonCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val annotationClassId = ClassId.fromString(ChameleonAnnotationString)

        val snapshotFunctionString = requireNotNull(
            configuration.get(ChameleonCommandLineProcessor.CompilerOptions.SnapshotFunction),
        )
        val snapshotFunctionCallableId = snapshotFunctionString.toMemberCallableId()

        val themeVariantEnumString = requireNotNull(
            configuration.get(ChameleonCommandLineProcessor.CompilerOptions.ThemeVariantEnum),
        )
        val themeVariantClassId = ClassId.fromString(themeVariantEnumString)

        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE,
        )

        IrGenerationExtension.registerExtension(
            extension = ChameleonIrGenerationExtension(
                chameleonAnnotationId = annotationClassId,
                snapshotFunctionId = snapshotFunctionCallableId,
                themeVariantEnumId = themeVariantClassId,
                messageCollector = messageCollector,
            ),
        )
    }
}

internal const val ChameleonAnnotationString = "io/github/reactivecircus/chameleon/runtime/Chameleon"
