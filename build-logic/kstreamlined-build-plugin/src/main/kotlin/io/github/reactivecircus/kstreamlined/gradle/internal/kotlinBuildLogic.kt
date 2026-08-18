package io.github.reactivecircus.kstreamlined.gradle.internal

import isInIdeaSync
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
context(project: Project)
internal fun KotlinBaseExtension.configureKotlin(
    enableExplicitApi: Boolean = true,
) {
    if (this is KotlinMultiplatformExtension) {
        compilerOptions {
            progressiveMode.set(true)
            optIn.addAll(OptIns)
            freeCompilerArgs.addAll(computeFreeCompilerArgs())
        }
    } else {
        project.tasks.withType(KotlinJvmCompile::class.java).configureEach {
            it.compilerOptions {
                progressiveMode.set(true)
                optIn.addAll(OptIns)
                freeCompilerArgs.addAll(computeFreeCompilerArgs())
            }
        }
    }
    configureJvmCompatibility()
    if (enableExplicitApi) {
        explicitApi()
    }
}

private val OptIns = listOf(
    "kotlin.time.ExperimentalTime",
    "kotlin.experimental.ExperimentalObjCName",
)

context(project: Project)
private fun computeFreeCompilerArgs() = buildList {
    addAll(
        listOf(
            "-Xcollection-literals",
            "-Xconsistent-data-class-copy-visibility",
            "-Xexplicit-context-arguments",
            "-Xintrinsic-const-evaluation",
            "-Xname-based-destructuring=complete",
        ),
    )
    // TODO remove once supported by AS
    if (project.isInIdeaSync) {
        add("-Xexplicit-backing-fields")
    }
}

context(project: Project)
private fun configureJvmCompatibility() {
    project.tasks.withType(KotlinJvmCompile::class.java).configureEach {
        it.compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
    }
    project.tasks.withType(JavaCompile::class.java).configureEach {
        it.sourceCompatibility = JavaVersion.VERSION_21.toString()
        it.targetCompatibility = JavaVersion.VERSION_21.toString()
    }
}
