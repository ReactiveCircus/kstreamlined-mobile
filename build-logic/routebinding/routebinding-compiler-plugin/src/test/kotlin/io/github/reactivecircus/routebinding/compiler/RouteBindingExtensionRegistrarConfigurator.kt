package io.github.reactivecircus.routebinding.compiler

import io.github.reactivecircus.routebinding.compiler.ir.RouteBindingIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.RuntimeClasspathProvider
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

fun TestConfigurationBuilder.configurePlugin() {
    useConfigurators(
        ::RouteBindingExtensionRegistrarConfigurator,
        ::RouteBindingRuntimeEnvironmentConfigurator,
    )
    useCustomRuntimeClasspathProviders(::RouteBindingRuntimeClassPathProvider)
    useAdditionalSourceProviders(::AdditionalFilesProvider)
    configureMetroRuntime()
}

private class RouteBindingExtensionRegistrarConfigurator(
    testServices: TestServices,
) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        IrGenerationExtension.registerExtension(
            RouteBindingIrGenerationExtension(
                messageCollector = configuration.get(
                    CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    MessageCollector.NONE,
                ),
            ),
        )
    }
}

private val RouteBindingRuntimeClasspath =
    System.getProperty("routeBindingRuntime.classpath")?.split(File.pathSeparator)?.map(::File)
        ?: error("Unable to get a valid classpath from 'routeBindingRuntime.classpath' property")

private class RouteBindingRuntimeEnvironmentConfigurator(
    testServices: TestServices,
) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        configuration.addJvmClasspathRoots(RouteBindingRuntimeClasspath)
    }
}

private class RouteBindingRuntimeClassPathProvider(
    testServices: TestServices,
) : RuntimeClasspathProvider(testServices) {
    override fun runtimeClassPaths(module: TestModule): List<File> = RouteBindingRuntimeClasspath
}
