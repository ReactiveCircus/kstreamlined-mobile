package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure Kotlin compiler options, language settings, JVM compatibility for the [target].
 */
internal fun KotlinProjectExtension.configureKotlin(
    target: Project,
) {
    sourceSets.configureEach {
        it.languageSettings {
            progressiveMode = true
            optIn("kotlin.time.ExperimentalTime")
            optIn("kotlin.experimental.ExperimentalObjCName")
            enableLanguageFeature("BreakContinueInInlineLambdas")
            enableLanguageFeature("ContextParameters")
            enableLanguageFeature("DataClassCopyRespectsConstructorVisibility")
            enableLanguageFeature("ExplicitBackingFields")
            enableLanguageFeature("MultiDollarInterpolation")
            enableLanguageFeature("NestedTypeAliases")
            enableLanguageFeature("WhenGuards")
        }
    }
    target.configureJvmCompatibility()
    explicitApi()
}

/**
 * Same as [configureKotlin] above but for Android Gradle Plugin's built-in Kotlin.
 */
internal fun KotlinBaseExtension.configureBuiltInKotlin(
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
                "-XXLanguage:+BreakContinueInInlineLambdas",
                "-XXLanguage:+ContextParameters",
                "-XXLanguage:+DataClassCopyRespectsConstructorVisibility",
                "-XXLanguage:+ExplicitBackingFields",
                "-XXLanguage:+MultiDollarInterpolation",
                "-XXLanguage:+NestedTypeAliases",
                "-XXLanguage:+WhenGuards",
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
            jvmTarget.set(JvmTarget.JVM_17)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
    }
    tasks.withType(JavaCompile::class.java).configureEach {
        it.sourceCompatibility = JavaVersion.VERSION_17.toString()
        it.targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}
