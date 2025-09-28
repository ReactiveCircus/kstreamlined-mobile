package io.github.reactivecircus.cocoon.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

public class CocoonCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "io.github.reactivecircus.cocoon.compiler"

    @Suppress("MaxLineLength")
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = CompilerOptions.Annotation.toString(),
            valueDescription = "Fully qualified annotation class name",
            description = "The fully qualified name of the annotation to be used for marking functions for transformation.",
        ),
        CliOption(
            optionName = CompilerOptions.WrappingFunction.toString(),
            valueDescription = "Fully qualified name of the higher-order function to be used for wrapping the transformed function's body.",
            description = "The fully qualified name of the function to be used for wrapping the transformed function.",
        ),
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            CompilerOptions.Annotation.toString() -> configuration.put(CompilerOptions.Annotation, value)
            CompilerOptions.WrappingFunction.toString() -> configuration.put(CompilerOptions.WrappingFunction, value)
            else -> throw IllegalArgumentException("Unknown plugin option: ${option.optionName}")
        }
    }

    internal object CompilerOptions {
        val Annotation = CompilerConfigurationKey<String>("annotation")
        val WrappingFunction = CompilerConfigurationKey<String>("wrappingFunction")
    }
}
