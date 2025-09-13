package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure Kotlin compiler options, language settings, JVM toolchain for all projects.
 */
internal fun KotlinProjectExtension.configureKotlin(
    target: Project,
    enableExplicitApi: Boolean = true,
) {
    target.tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            progressiveMode.set(true)
            optIn.addAll(
                "kotlin.time.ExperimentalTime",
                "kotlin.experimental.ExperimentalObjCName",
            )
            freeCompilerArgs.addAll(
                "-XXLanguage:+BreakContinueInInlineLambdas",
                "-XXLanguage:+ContextParameters",
                "-XXLanguage:+ContextSensitiveResolutionUsingExpectedType",
                "-XXLanguage:+DataClassCopyRespectsConstructorVisibility",
                "-XXLanguage:+ExplicitBackingFields",
                "-XXLanguage:+MultiDollarInterpolation",
                "-XXLanguage:+NestedTypeAliases",
                "-XXLanguage:+WhenGuards",
            )
        }
    }
    target.tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
    }
    target.tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
    if (enableExplicitApi) {
        explicitApi()
    }
}
