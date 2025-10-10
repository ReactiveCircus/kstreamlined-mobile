package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import isAppleSilicon
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind

internal fun KotlinMultiplatformExtension.configureKmpTargets(
    project: Project,
    moduleType: KmpModuleType,
) {
    if (moduleType.jvmTargetEnabled()) {
        jvm()
    }

    if (moduleType.androidTargetEnabled()) {
        extensions.configure(KotlinMultiplatformAndroidLibraryTarget::class.java) {
            it.configureKmpAndroidLibraryExtension(project)
        }
    }

    if (moduleType.iosTargetEnabled()) {
        iosArm64()
        if (project.isAppleSilicon) {
            iosSimulatorArm64()
        } else {
            iosX64()
        }
    }
}

internal enum class KmpModuleType {
    JvmAndIos,
    AndroidAndIos,
    IosOnly,
    ;

    fun jvmTargetEnabled(): Boolean = this == JvmAndIos

    fun androidTargetEnabled(): Boolean = this == AndroidAndIos

    fun iosTargetEnabled(): Boolean = this == JvmAndIos || this == AndroidAndIos || this == IosOnly
}

/**
 * Apply test configs to KMP project.
 */
internal fun KotlinMultiplatformExtension.configureKmpTest() {
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
    targets.withType(KotlinNativeTarget::class.java).configureEach {
        it.binaries.configureEach {
            if (it.outputKind == NativeOutputKind.TEST) {
                it.linkerOpts("-lsqlite3")
            }
        }
    }
}
