package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import isAppleSilicon
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind

/**
 * Apply common configs to KMP project.
 */
internal fun KotlinMultiplatformExtension.configureKMPCommon(
    project: Project,
    enableJvmTarget: Boolean = true,
    enableAndroidTarget: Boolean = false,
) {
    if (enableJvmTarget) {
        jvm()
    }
    if (enableAndroidTarget) {
        androidTarget()
    }
    iosArm64()
    if (project.isAppleSilicon) {
        iosSimulatorArm64()
    } else {
        iosX64()
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
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.configureEach {
            if (outputKind == NativeOutputKind.TEST) {
                linkerOpts("-lsqlite3")
            }
        }
    }
}
