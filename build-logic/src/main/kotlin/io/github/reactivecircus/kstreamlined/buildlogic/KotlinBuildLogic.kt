package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Kotlin compiler options, language settings, JVM toolchain for all projects.
 */
@Suppress("MagicNumber")
internal fun KotlinProjectExtension.configureKotlin(
    target: Project,
    enableExplicitApi: Boolean = true,
) {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
        vendor.set(JvmVendorSpec.AZUL)
    }
    target.tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                "-Xjvm-default=all",
            )
        }
    }
    sourceSets.configureEach {
        languageSettings {
            applyLanguageSettings()
        }
    }
    if (enableExplicitApi) {
        explicitApi()
    }
}
