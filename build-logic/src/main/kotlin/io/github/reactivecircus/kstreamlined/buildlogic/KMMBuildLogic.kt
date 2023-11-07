package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Apply common configs to KMM project.
 */
internal fun KotlinMultiplatformExtension.configureKMMCommon(
    project: Project,
    enableJvmTarget: Boolean = true,
) {
    if (enableJvmTarget) {
        jvm()
        configureKotlinJvm(project)
    }
    iosArm64()
    if (project.isAppleSilicon) {
        iosSimulatorArm64()
    } else {
        iosX64()
    }

    sourceSets.configureEach {
        languageSettings {
            applyLanguageSettings()
        }
    }
}

/**
 * Apply test configs to KMM project.
 */
internal fun KotlinMultiplatformExtension.configureKMMTest() {
    with(sourceSets) {
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

/**
 * Configure Kotlin common compile options.
 */
internal fun Project.configureKotlinCommonCompileOptions() {
    tasks.withType<KotlinCommonCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }
}
