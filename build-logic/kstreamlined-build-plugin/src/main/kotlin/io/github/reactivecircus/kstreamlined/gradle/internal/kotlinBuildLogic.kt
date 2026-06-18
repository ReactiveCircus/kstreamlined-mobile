package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure Kotlin compiler options, language settings, JVM compatibility for the [target].
 */
internal fun KotlinBaseExtension.configureKotlin(
    target: Project,
    enableExplicitApi: Boolean = true,
) {
    if (this is KotlinMultiplatformExtension) {
        compilerOptions {
            progressiveMode.set(true)
            optIn.addAll(OptIns)
            freeCompilerArgs.addAll(FreeCompilerArgs)
        }
    } else {
        target.tasks.withType(KotlinJvmCompile::class.java).configureEach {
            it.compilerOptions {
                progressiveMode.set(true)
                optIn.addAll(OptIns)
                freeCompilerArgs.addAll(FreeCompilerArgs)
            }
        }
    }
    target.configureJvmCompatibility()
    if (enableExplicitApi) {
        explicitApi()
    }
}

private val OptIns = listOf(
    "kotlin.time.ExperimentalTime",
    "kotlin.experimental.ExperimentalObjCName",
)

private val FreeCompilerArgs = listOf(
    "-Xcollection-literals",
    "-Xconsistent-data-class-copy-visibility",
    "-Xexplicit-backing-fields", // TODO remove once AS migrates to IJ 2026.1.4
    "-Xexplicit-context-arguments",
    "-Xintrinsic-const-evaluation",
    "-Xname-based-destructuring=complete",
)

private fun Project.configureJvmCompatibility() {
    tasks.withType(KotlinJvmCompile::class.java).configureEach {
        it.compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
    }
    tasks.withType(JavaCompile::class.java).configureEach {
        it.sourceCompatibility = JavaVersion.VERSION_21.toString()
        it.targetCompatibility = JavaVersion.VERSION_21.toString()
    }
}
