package io.github.reactivecircus.routebinding.compiler

import io.github.reactivecircus.routebinding.compiler.fir.RouteBindingDeclarationGenerationExtension
import io.github.reactivecircus.routebinding.compiler.fir.RouteBindingFirExtensionRegistrar
import io.github.reactivecircus.routebinding.compiler.ir.RouteBindingIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar.ExtensionRegistrarContext
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

public class RouteBindingCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val pluginId: String get() = RouteBindingPluginId

    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE,
        )

        FirExtensionRegistrarAdapter.registerExtension(RouteBindingFirExtensionRegistrar)

        IrGenerationExtension.registerExtension(
            extension = RouteBindingIrGenerationExtension(
                messageCollector = messageCollector,
            ),
        )
    }
}
