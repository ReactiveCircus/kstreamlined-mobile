@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("UnstableApiUsage")

package io.github.reactivecircus.kstreamlined.gradle.internal

import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyCollector
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinDependencies
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.configureJvmTopLevelDependencies(
    sourceSets: NamedDomainObjectContainer<KotlinSourceSet>,
    dependenciesBlock: Action<KotlinDependencies>,
) {
    val kotlinDependencies = project.objects.newInstance(KotlinDependenciesImpl::class.java)

    val main = sourceSets.getByName("main")
    val test = sourceSets.getByName("test")

    infix fun DependencyCollector.wireWith(configurationName: String) {
        val configuration = project.configurations.getByName(configurationName)
        configuration.fromDependencyCollector(this)
    }

    kotlinDependencies.api wireWith main.apiConfigurationName
    kotlinDependencies.implementation wireWith main.implementationConfigurationName
    kotlinDependencies.compileOnly wireWith main.compileOnlyConfigurationName
    kotlinDependencies.runtimeOnly wireWith main.runtimeOnlyConfigurationName

    kotlinDependencies.testImplementation wireWith test.implementationConfigurationName
    kotlinDependencies.testCompileOnly wireWith test.compileOnlyConfigurationName
    kotlinDependencies.testRuntimeOnly wireWith test.runtimeOnlyConfigurationName

    dependenciesBlock.execute(kotlinDependencies)
}

internal fun Project.configureAndroidTopLevelDependencies(
    sourceSets: NamedDomainObjectContainer<out AndroidSourceSet>,
    dependenciesBlock: Action<KotlinDependencies>,
) {
    val kotlinDependencies = project.objects.newInstance(KotlinDependenciesImpl::class.java)

    val main = sourceSets.getByName("main")
    val test = sourceSets.findByName("test")

    infix fun DependencyCollector.wireWith(configurationName: String) {
        val configuration = project.configurations.getByName(configurationName)
        configuration.fromDependencyCollector(this)
    }

    kotlinDependencies.api wireWith main.apiConfigurationName
    kotlinDependencies.implementation wireWith main.implementationConfigurationName
    kotlinDependencies.compileOnly wireWith main.compileOnlyConfigurationName
    kotlinDependencies.runtimeOnly wireWith main.runtimeOnlyConfigurationName

    if (test != null) {
        kotlinDependencies.testImplementation wireWith test.implementationConfigurationName
        kotlinDependencies.testCompileOnly wireWith test.compileOnlyConfigurationName
        kotlinDependencies.testRuntimeOnly wireWith test.runtimeOnlyConfigurationName
    }

    dependenciesBlock.execute(kotlinDependencies)
}

private abstract class KotlinDependenciesImpl : KotlinDependencies {
    override fun kotlin(module: String) = kotlin(module, null)

    override fun kotlin(module: String, version: String?): Dependency = project.dependencyFactory
        .create("org.jetbrains.kotlin", "kotlin-$module", version)
}
