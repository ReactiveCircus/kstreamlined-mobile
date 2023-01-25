package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure Kotlin JVM toolchain and compiler options.
 */
internal fun KotlinProjectExtension.configureKotlinJvm(target: Project) {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
    target.tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.set(
                buildList {
                    if (project.providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
                        addAll(composeCompilerMetricsArgs(target.buildDir))
                        addAll(composeCompilerReportsArgs(target.buildDir))
                    }
                }
            )
        }
    }
}
