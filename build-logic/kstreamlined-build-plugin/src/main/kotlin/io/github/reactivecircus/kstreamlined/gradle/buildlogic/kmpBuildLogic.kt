package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import isAppleSilicon
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind

/**
 * Enable and configure KMP targets.
 */
internal fun KotlinMultiplatformExtension.configureKmpTargets(
    project: Project,
    config: KmpTargetsConfig,
) {
    if (config.jvmTargetEnabled) {
        jvm()
    }

    if (config.androidTargetEnabled) {
        extensions.configure(KotlinMultiplatformAndroidLibraryTarget::class.java) {
            it.configureKmpAndroidLibraryExtension(
                project = project,
                namespace = config.androidNamespace!!,
                hostTestsEnabled = config.androidHostTestsEnabled,
            )
        }
    }

    if (config.iosTargetEnabled) {
        iosArm64()
        if (project.isAppleSilicon) {
            iosSimulatorArm64()
        } else {
            iosX64()
        }
    }
}

internal class KmpTargetsConfig(
    val jvmTargetEnabled: Boolean,
    val androidTargetEnabled: Boolean,
    val iosTargetEnabled: Boolean,
    val androidNamespace: String?,
    val androidHostTestsEnabled: Boolean,
)

/**
 * Apply test configs to KMP project.
 */
internal fun KotlinMultiplatformExtension.configureKmpTest(project: Project) {
    with(sourceSets) {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
    targets.withType(KotlinNativeTarget::class.java).configureEach { target ->
        target.binaries.configureEach {
            if (it.outputKind == NativeOutputKind.TEST) {
                it.linkerOpts("-lsqlite3")
            }
        }
    }
    project.configureTest()
}
