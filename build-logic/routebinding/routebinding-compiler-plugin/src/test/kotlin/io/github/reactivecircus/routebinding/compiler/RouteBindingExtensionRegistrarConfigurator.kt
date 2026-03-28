package io.github.reactivecircus.routebinding.compiler

import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import dev.zacsweers.metro.compiler.MetroCommandLineProcessor
import dev.zacsweers.metro.compiler.MetroCompilerPluginRegistrar
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
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
    useDirectives(RouteBindingDirectives)
    useCustomRuntimeClasspathProviders(::RouteBindingRuntimeClassPathProvider)
    useAdditionalSourceProviders(::AdditionalFilesProvider)
    configureMetroRuntime()
    configureComposeRuntime()
}

private class RouteBindingExtensionRegistrarConfigurator(
    testServices: TestServices,
) : EnvironmentConfigurator(testServices) {
    private val metroCliProcessor = MetroCommandLineProcessor()
    private val metroRegistrar = MetroCompilerPluginRegistrar()
    private val composeRegistrar = ComposePluginRegistrar()
    private val routeBindingRegistrar = RouteBindingCompilerPluginRegistrar()

    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        // register RouteBinding compiler plugin
        with(routeBindingRegistrar) { registerExtensions(configuration) }

        // configure and register Metro compiler plugin
        val generateContributionHintsInFir = metroCliProcessor.pluginOptions.first { it.optionName == "generate-contribution-hints-in-fir" }
        metroCliProcessor.processOption(generateContributionHintsInFir, "true", configuration)
        val contributesAsInject = metroCliProcessor.pluginOptions.first { it.optionName == "contributes-as-inject" }
        metroCliProcessor.processOption(contributesAsInject, "true", configuration)
        with(metroRegistrar) { registerExtensions(configuration) }

        // register Compose compiler plugin if enabled from directive
        val enableComposeCompiler = RouteBindingDirectives.ENABLE_COMPOSE_COMPILER in module.directives
        if (enableComposeCompiler) {
            with(composeRegistrar) { registerExtensions(configuration) }
        }
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
