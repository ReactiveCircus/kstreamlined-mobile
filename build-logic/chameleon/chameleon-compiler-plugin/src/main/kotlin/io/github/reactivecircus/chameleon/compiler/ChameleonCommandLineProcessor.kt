package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

public class ChameleonCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "io.github.reactivecircus.chameleon.compiler"

    @Suppress("MaxLineLength")
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = CompilerOptions.SnapshotFunction.toString(),
            valueDescription = "String",
            description = "The fully qualified name of the snapshot function to be used for passing in the `themeVariant`.",
        ),
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            CompilerOptions.SnapshotFunction.toString() -> configuration.put(CompilerOptions.SnapshotFunction, value)
            else -> throw IllegalArgumentException("Unknown plugin option: ${option.optionName}")
        }
    }

    internal object CompilerOptions {
        // TODO check format (include fqn, class? or need separate `SnapshotTesterClass`?)
        val SnapshotFunction = CompilerConfigurationKey<String>("snapshotFunction")
        // TODO add ThemeVariantEnum - find argument in SnapshotFunction by type and assign
    }
}
