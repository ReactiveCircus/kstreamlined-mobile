package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure Kotlin compiler options, language settings, JVM compatibility for the [target].
 */
internal fun KotlinBaseExtension.configureKotlin(
    target: Project,
    enableExplicitApi: Boolean = true,
) {
    target.tasks.withType(KotlinCompilationTask::class.java).configureEach {
        it.compilerOptions {
            progressiveMode.set(true)
            optIn.addAll(
                "kotlin.time.ExperimentalTime",
                "kotlin.experimental.ExperimentalObjCName",
            )
            freeCompilerArgs.addAll(
                "-Xnon-local-break-continue",
                "-Xcontext-parameters",
                "-Xconsistent-data-class-copy-visibility",
                "-Xexplicit-backing-fields",
                "-Xmulti-dollar-interpolation",
                "-Xnested-type-aliases",
                "-Xwhen-guards",
            )
        }
    }
    target.configureJvmCompatibility()
    if (enableExplicitApi) {
        explicitApi()
    }
}

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
