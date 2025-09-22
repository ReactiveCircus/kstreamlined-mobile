package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
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
    target.tasks.withType(KotlinCompile::class.java).configureEach {
        it.compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        }
    }
    target.tasks.withType(JavaCompile::class.java).configureEach {
        it.sourceCompatibility = JavaVersion.VERSION_17.toString()
        it.targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    sourceSets.configureEach {
        it.languageSettings {
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
