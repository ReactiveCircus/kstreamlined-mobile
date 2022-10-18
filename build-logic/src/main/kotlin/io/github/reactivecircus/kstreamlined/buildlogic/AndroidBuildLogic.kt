package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestedExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
@Suppress("UnstableApiUsage")
internal fun TestedExtension.configureCommonAndroidOptions(project: Project) {
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
        isCoreLibraryDesugaringEnabled = true
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

    with(project) {
        dependencies.add("coreLibraryDesugaring", the<LibrariesForLibs>().desugarJdkLibs)
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
internal fun LibraryAndroidComponentsExtension.configureAndroidLibraryVariants() {
    // disable release build variant for all Android library projects
    beforeVariants(selector().withBuildType("release")) {
        it.enable = false
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
