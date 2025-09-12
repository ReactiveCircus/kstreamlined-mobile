package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
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
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
            // TODO investigate:
            //  why `-Xskip-prerelease-check` is needed when compiling Android library projects with experimental language features enabled (see https://youtrack.jetbrains.com/issue/KT-75588)
            //  why opt-in from languageSettings DSL no longer works for Android library projects
            freeCompilerArgs.addAll(
                "-Xskip-prerelease-check"
            )
            optIn.addAll(
                "kotlin.time.ExperimentalTime",
                "kotlin.experimental.ExperimentalObjCName",
            )
        }
    }
    target.tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
    sourceSets.configureEach {
        languageSettings {
            progressiveMode = true
            optIn("kotlin.time.ExperimentalTime")
            optIn("kotlin.experimental.ExperimentalObjCName")
            enableLanguageFeature("BreakContinueInInlineLambdas")
            enableLanguageFeature("ContextParameters")
            enableLanguageFeature("ContextSensitiveResolutionUsingExpectedType")
            enableLanguageFeature("DataClassCopyRespectsConstructorVisibility")
            enableLanguageFeature("ExplicitBackingFields")
            enableLanguageFeature("MultiDollarInterpolation")
            enableLanguageFeature("NestedTypeAliases")
            enableLanguageFeature("WhenGuards")
        }
    }
    if (enableExplicitApi) {
        explicitApi()
    }
}
