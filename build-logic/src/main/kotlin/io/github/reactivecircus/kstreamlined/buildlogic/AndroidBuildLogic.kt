package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.TestAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestedExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
@Suppress("UnstableApiUsage")
internal fun TestedExtension.configureCommonAndroidOptions() {
    setCompileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion = androidSdk.buildTools

    defaultConfig {
        minSdk = androidSdk.minSdk
        targetSdk = androidSdk.targetSdk

        // only support English for now
        resourceConfigurations.add("en")
    }

    testOptions.animationsDisabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        resources {
            pickFirsts.addAll(
                listOf(
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1",
                )
            )
        }
    }
}

/**
 * Apply baseline configurations for all Android Test projects.
 */
@Suppress("UnstableApiUsage")
internal fun TestExtension.configureAndroidTestOptions() {
    setCompileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion = androidSdk.buildTools

    defaultConfig {
        minSdk = androidSdk.minSdk
        targetSdk = androidSdk.targetSdk

        // only support English for now
        resourceConfigurations.add("en")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

/**
 * Configure the Application Library Component based on build variants.
 */
internal fun LibraryAndroidComponentsExtension.configureAndroidLibraryVariants(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            beforeVariants { it.enableUnitTest = false }
        }

        // disable android test tasks if the androidTest source set is empty
        if (!project.hasAndroidTestSource) {
            beforeVariants { it.enableAndroidTest = false }
        }
    }

    // disable android test tasks for Kotlin Multiplatform Android libraries
    project.plugins.withType<KotlinMultiplatformPluginWrapper> {
        beforeVariants { it.enableAndroidTest = false }
    }
}

/**
 * Configure the Application Android Component based on build variants.
 */
internal fun ApplicationAndroidComponentsExtension.configureAndroidApplicationVariants(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            beforeVariants { it.enableUnitTest = false }
        }

        // disable android test tasks if the androidTest source set is empty
        if (!project.hasAndroidTestSource) {
            beforeVariants { it.enableAndroidTest = false }
        }
    }
}

/**
 * Configure the Application Test Component based on build variants.
 */
internal fun TestAndroidComponentsExtension.configureAndroidTestVariants(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            beforeVariants { it.enableUnitTest = false }
        }
    }
}

/**
 * Enable Compose and configure Compose options
 */
internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    @Suppress("UnstableApiUsage")
    commonExtension.apply {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = the<LibrariesForLibs>().versions.androidx.compose.compiler.get()
    }
}
