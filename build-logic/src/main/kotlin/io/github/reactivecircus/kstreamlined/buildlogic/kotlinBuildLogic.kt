package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Kotlin compiler options, language settings, JVM toolchain for all projects.
 */
internal fun KotlinProjectExtension.configureKotlin(
    target: Project,
    enableExplicitApi: Boolean = true,
) {
    target.tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll(
                "-Xjvm-default=all",
                "-Xconsistent-data-class-copy-visibility",
            )
        }
    }
    target.tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
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
