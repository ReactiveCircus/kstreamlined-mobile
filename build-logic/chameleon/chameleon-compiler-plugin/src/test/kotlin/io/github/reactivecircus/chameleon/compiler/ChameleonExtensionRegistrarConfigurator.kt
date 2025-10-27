package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.RuntimeClasspathProvider
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

fun TestConfigurationBuilder.configurePlugin() {
    useConfigurators(
        ::ChameleonExtensionRegistrarConfigurator,
        ::ChameleonRuntimeEnvironmentConfigurator,
    )
    useDirectives(ChameleonDirectives)
    useCustomRuntimeClasspathProviders(::ChameleonRuntimeClassPathProvider)
}

private class ChameleonExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        val snapshotFunctionString = module.directives[ChameleonDirectives.SNAPSHOT_FUNCTION].first()
        val themeVariantEnumString = module.directives[ChameleonDirectives.THEME_VARIANT_ENUM].first()

        IrGenerationExtension.registerExtension(
            ChameleonIrGenerationExtension(
                chameleonAnnotationId = ClassId.fromString(ChameleonAnnotationString),
                snapshotFunctionId = snapshotFunctionString.toMemberCallableId(),
                themeVariantEnumId = ClassId.fromString(themeVariantEnumString),
                messageCollector = configuration.get(
                    CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    MessageCollector.NONE,
                ),
            )
        )
    }
}

private val ChameleonRuntimeClasspath =
    System.getProperty("chameleonRuntime.classpath")?.split(File.pathSeparator)?.map(::File)
        ?: error("Unable to get a valid classpath from 'chameleonRuntime.classpath' property")

private class ChameleonRuntimeEnvironmentConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        configuration.addJvmClasspathRoots(ChameleonRuntimeClasspath)
    }
}

private class ChameleonRuntimeClassPathProvider(testServices: TestServices) : RuntimeClasspathProvider(testServices) {
    override fun runtimeClassPaths(module: TestModule): List<File> = ChameleonRuntimeClasspath
}
