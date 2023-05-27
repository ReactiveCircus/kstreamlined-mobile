package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.gradle.internal.tasks.PerModuleReportDependenciesTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

/**
 * Configure JUnit test options if applicable.
 */
internal fun Project.configureTest() {
    tasks.withType<Test>().configureEach {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
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
    dependencies.add("detektPlugins", the<LibrariesForLibs>().plugin.detektFormatting)

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            source.from(files("src/"))
            config.from(files("${project.rootDir}/detekt.yml"))
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
        }
        tasks.withType<Detekt>().configureEach {
            jvmTarget = JvmTarget.JVM_11.target
            reports {
                xml.required.set(false)
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
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
