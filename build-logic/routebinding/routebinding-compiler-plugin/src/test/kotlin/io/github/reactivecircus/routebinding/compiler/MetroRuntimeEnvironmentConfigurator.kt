package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.RuntimeClasspathProvider
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

fun TestConfigurationBuilder.configureMetroRuntime() {
    useConfigurators(::MetroRuntimeEnvironmentConfigurator)
    useCustomRuntimeClasspathProviders(::MetroRuntimeClassPathProvider)
}

private val MetroRuntimeClasspath =
    System.getProperty("metroRuntime.classpath")?.split(File.pathSeparator)?.map(::File)
        ?: error("Unable to get a valid classpath from 'metroRuntime.classpath' property")

private class MetroRuntimeEnvironmentConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration,
    ) {
        configuration.addJvmClasspathRoots(MetroRuntimeClasspath)
    }
}

private class MetroRuntimeClassPathProvider(testServices: TestServices) : RuntimeClasspathProvider(testServices) {
    override fun runtimeClassPaths(module: TestModule): List<File> = MetroRuntimeClasspath
}
