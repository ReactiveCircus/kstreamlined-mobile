package io.github.reactivecircus.kstreamlined.gradle.internal

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind

/**
 * Enable and configure KMP targets.
 */
context(project: Project)
internal fun KotlinMultiplatformExtension.configureKmpTargets(
    config: KmpTargetsConfig,
) {
    if (config.jvmTargetEnabled) {
        jvm()
    }

    if (config.androidTargetEnabled) {
        this@configureKmpTargets.extensions.configure(KotlinMultiplatformAndroidLibraryTarget::class.java) {
            it.configureKmpAndroidLibraryExtension(
                namespace = config.androidNamespace!!,
                hostTestsEnabled = config.androidHostTestsEnabled,
            )
        }
    }

    if (config.iosTargetEnabled) {
        iosArm64()
        iosSimulatorArm64()
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
context(project: Project)
internal fun KotlinMultiplatformExtension.configureKmpTest() {
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
