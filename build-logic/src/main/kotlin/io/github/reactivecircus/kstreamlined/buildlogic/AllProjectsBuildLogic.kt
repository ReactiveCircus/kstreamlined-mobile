package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.gradle.internal.tasks.PerModuleReportDependenciesTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

/**
 * Configure Kotlin JVM compile options.
 */
internal fun Project.configureKotlinJvmCompileOptions() {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()

            @OptIn(ExperimentalStdlibApi::class)
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs + buildList {
                if (providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                    addAll(composeCompilerMetricsArgs(buildDir))
                    addAll(composeCompilerReportsArgs(buildDir))
                }
            }
        }
    }
}

/**
 * Configure JUnit test options if applicable.
 */
internal fun Project.configureTest() {
    tasks.withType<Test> {
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

/**
 * Apply and configure detekt.
 */
internal fun Project.configureDetekt() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    val detektVersion = the<LibrariesForLibs>().versions.detekt.get()
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            source = files("src/")
            config = files("${project.rootDir}/detekt.yml")
            buildUponDefaultConfig = true
            allRules = true
        }
        tasks.withType<Detekt>().configureEach {
            reports {
                html.outputLocation.set(file("build/reports/detekt/${project.name}.html"))
            }
        }
    }
}

/**
 * Mark tasks that are incompatible with configuration cache to prevent build failure.
 */
internal fun Project.markNonCompatibleConfigurationCacheTasks() {
    tasks.withType<KotlinNativeLink>().configureEach {
        notCompatibleWithConfigurationCache("Kotlin Native doesn't fully support configuration cache yet.")
    }
    tasks.withType<KotlinNativeCompile>().configureEach {
        notCompatibleWithConfigurationCache("Kotlin Native doesn't fully support configuration cache yet.")
    }
    tasks.withType<KotlinNativeTest>().configureEach {
        notCompatibleWithConfigurationCache("Kotlin Native doesn't fully support configuration cache yet.")
    }
    tasks.withType<XCFrameworkTask>().configureEach {
        notCompatibleWithConfigurationCache("Kotlin Native doesn't fully support configuration cache yet.")
    }
    tasks.withType<FatFrameworkTask>().configureEach {
        notCompatibleWithConfigurationCache("Kotlin Native doesn't fully support configuration cache yet.")
    }
    tasks.withType<PerModuleReportDependenciesTask>().configureEach {
        notCompatibleWithConfigurationCache("This AGP task doesn't support configuration cache.")
    }
}
