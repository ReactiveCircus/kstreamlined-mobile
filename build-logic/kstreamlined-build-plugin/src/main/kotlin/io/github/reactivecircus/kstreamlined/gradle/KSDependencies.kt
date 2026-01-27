package io.github.reactivecircus.kstreamlined.gradle

import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.Dependencies
import org.gradle.api.artifacts.dsl.DependencyCollector
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public sealed interface KSDependencies : Dependencies {
    public val implementation: DependencyCollector
    public val api: DependencyCollector

    public abstract class Jvm : KSDependencies, HasTestConfiguration

    public sealed interface Android {
        public abstract class Library : KSDependencies, HasTestConfiguration

        public abstract class Test : KSDependencies

        public abstract class App : KSDependencies, HasTestConfiguration {
            public abstract val debugImplementation: DependencyCollector
            public abstract val releaseImplementation: DependencyCollector
            public abstract val mockImplementation: DependencyCollector
            public abstract val devImplementation: DependencyCollector
            public abstract val demoImplementation: DependencyCollector
            public abstract val prodImplementation: DependencyCollector
        }
    }
}

public interface HasTestConfiguration {
    public val testImplementation: DependencyCollector
}

internal inline fun <reified S : Named, reified D : KSDependencies> Project.configureDependencies(
    sourceSets: NamedDomainObjectContainer<out S>,
    dependenciesBlock: Action<D>,
) {
    val dependencies = project.objects.newInstance(D::class.java)

    when (S::class) {
        KotlinSourceSet::class -> {
            val main = sourceSets.getByName("main") as KotlinSourceSet
            val test = sourceSets.getByName("test") as KotlinSourceSet
            require(dependencies is KSDependencies.Jvm)
            dependencies.api.wireWith(this, main.apiConfigurationName)
            dependencies.implementation.wireWith(this, main.implementationConfigurationName)
            dependencies.testImplementation.wireWith(this, test.implementationConfigurationName)
        }

        AndroidSourceSet::class -> {
            val main = sourceSets.getByName("main") as AndroidSourceSet
            dependencies.api.wireWith(this, main.apiConfigurationName)
            dependencies.implementation.wireWith(this, main.implementationConfigurationName)

            if (dependencies is HasTestConfiguration) {
                val test = sourceSets.getByName("test") as AndroidSourceSet
                dependencies.testImplementation.wireWith(this, test.implementationConfigurationName)
            }

            if (dependencies is KSDependencies.Android.App) {
                val debug = sourceSets.getByName("debug") as AndroidSourceSet
                val release = sourceSets.getByName("release") as AndroidSourceSet
                val mock = sourceSets.getByName("mock") as AndroidSourceSet
                val dev = sourceSets.getByName("dev") as AndroidSourceSet
                val demo = sourceSets.getByName("demo") as AndroidSourceSet
                val prod = sourceSets.getByName("prod") as AndroidSourceSet

                dependencies.debugImplementation.wireWith(this, debug.implementationConfigurationName)
                dependencies.releaseImplementation.wireWith(this, release.implementationConfigurationName)
                dependencies.mockImplementation.wireWith(this, mock.implementationConfigurationName)
                dependencies.devImplementation.wireWith(this, dev.implementationConfigurationName)
                dependencies.demoImplementation.wireWith(this, demo.implementationConfigurationName)
                dependencies.prodImplementation.wireWith(this, prod.implementationConfigurationName)
            }
        }
    }

    dependenciesBlock.execute(dependencies)
}

@Suppress("UnstableApiUsage")
private fun DependencyCollector.wireWith(
    project: Project,
    configurationName: String,
) {
    val configuration = project.configurations.getByName(configurationName)
    configuration.fromDependencyCollector(this)
}
