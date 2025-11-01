package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

public class ChameleonCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String get() = Chameleon.PLUGIN_ID

    @Suppress("MaxLineLength")
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = CompilerOptions.SnapshotFunction.toString(),
            valueDescription = "String",
            description = "The fully qualified name of the snapshot function with a `themeVariant` parameter.",
        ),
        CliOption(
            optionName = CompilerOptions.ThemeVariantEnum.toString(),
            valueDescription = "String",
            description = "The fully qualified name of the enum class for the `themeVariant` parameter.",
        ),
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            CompilerOptions.SnapshotFunction.toString() -> configuration.put(CompilerOptions.SnapshotFunction, value)
            CompilerOptions.ThemeVariantEnum.toString() -> configuration.put(CompilerOptions.ThemeVariantEnum, value)
            else -> throw IllegalArgumentException("Unknown plugin option: ${option.optionName}")
        }
    }

    internal object CompilerOptions {
        val SnapshotFunction = CompilerConfigurationKey<String>("snapshotFunction")
        val ThemeVariantEnum = CompilerConfigurationKey<String>("themeVariantEnum")
    }
}
