package io.github.reactivecircus.chameleon.compiler

import io.github.reactivecircus.chameleon.compiler.fir.ChameleonFirExtensionRegistrar
import io.github.reactivecircus.chameleon.compiler.ir.ChameleonIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.name.ClassId

public class ChameleonCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val pluginId: String get() = Chameleon.PLUGIN_ID

    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
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

        FirExtensionRegistrarAdapter.registerExtension(ChameleonFirExtensionRegistrar)

        IrGenerationExtension.registerExtension(
            extension = ChameleonIrGenerationExtension(
                chameleonAnnotationId = Chameleon.ANNOTATION_ID,
                snapshotFunctionId = snapshotFunctionCallableId,
                themeVariantEnumId = themeVariantClassId,
                messageCollector = messageCollector,
            ),
        )
    }
}
