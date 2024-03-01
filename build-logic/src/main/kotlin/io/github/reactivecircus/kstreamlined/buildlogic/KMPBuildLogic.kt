package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Apply common configs to KMP project.
 */
internal fun KotlinMultiplatformExtension.configureKMPCommon(
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
 * Apply test configs to KMP project.
 */
internal fun KotlinMultiplatformExtension.configureKMPTest() {
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
            }
        }
    }
}

/**
 * Configure Kotlin compile options.
 */
internal fun Project.configureKotlinCompileOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        with(compilerOptions.freeCompilerArgs) {
            addAll(commonCompilerArgs)
            // enable explicit API mode for non-test Kotlin compilations
            if (!name.contains("TestKotlin")) {
                add("-Xexplicit-api=strict")
            }
        }
    }
}
