package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
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
    }
    iosArm64()
    if (project.isAppleSilicon) {
        iosSimulatorArm64()
    } else {
        iosX64()
    }

    with(sourceSets) {
        val commonMain by getting
        val iosArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            if (project.isAppleSilicon) {
                val iosSimulatorArm64Main by getting
                iosSimulatorArm64Main.dependsOn(this)
            } else {
                val iosX64Main by getting
                iosX64Main.dependsOn(this)
            }
        }
    }

    sourceSets.all {
        languageSettings {
            applyLanguageSettings()
        }
    }
}

/**
 * Apply test configs to KMM project.
 */
internal fun KotlinMultiplatformExtension.configureKMMTest(
    project: Project,
    enableJvmTest: Boolean = true,
    enableIosTest: Boolean = true,
) {
    with(sourceSets) {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        if (enableJvmTest) {
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
        if (enableIosTest) {
            val iosArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosArm64Test.dependsOn(this)
                if (project.isAppleSilicon) {
                    val iosSimulatorArm64Test by getting
                    iosSimulatorArm64Test.dependsOn(this)
                } else {
                    val iosX64Test by getting
                    iosX64Test.dependsOn(this)
                }
            }
        }
    }
}

/**
 * Configure Kotlin common compile options.
 */
internal fun Project.configureKotlinCommonCompileOptions() {
    tasks.withType<KotlinCommonCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }
}
