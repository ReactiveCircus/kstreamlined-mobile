package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

public class RouteBindingCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String get() = RouteBinding.PLUGIN_ID

    override val pluginOptions: Collection<AbstractCliOption> = listOf()
}
