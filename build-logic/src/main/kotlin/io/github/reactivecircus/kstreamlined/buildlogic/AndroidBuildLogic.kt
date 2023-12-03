package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestedExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
internal fun TestedExtension.configureCommonAndroidOptions(project: Project) {
    setCompileSdkVersion(AndroidSdk.compileSdk)
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk

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

    (this@configureCommonAndroidOptions as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        if (project.providers.gradleProperty("enableComposeCompilerReports").orNull == "true") {
            freeCompilerArgs += buildList {
                addAll(composeCompilerMetricsArgs(project.layout.buildDirectory).get())
                addAll(composeCompilerReportsArgs(project.layout.buildDirectory).get())
                addAll(composeCompilerStrongSkippingArgs)
            }
        }
    }
}

/**
 * Apply baseline configurations for all Android Test projects.
 */
internal fun TestExtension.configureAndroidTestOptions() {
    setCompileSdkVersion(AndroidSdk.compileSdk)
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk

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
    // disable debug build variant for all Android library projects
    beforeVariants(selector().withBuildType("debug")) {
        it.enable = false
    }
}

/**
 * Enable Compose and configure Compose options
 */
internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = the<LibrariesForLibs>().versions.androidx.compose.compiler.get()
    }
}
