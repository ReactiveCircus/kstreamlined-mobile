package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

public class RouteBindingCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String get() = RouteBindingPluginId

    override val pluginOptions: Collection<AbstractCliOption> = listOf()
}
