@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestedExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

/**
 * Configure the Application Component based on build variants.
 */
internal fun ApplicationAndroidComponentsExtension.configureAndroidApplicationVariants() {
    beforeVariants {
        // disable unit tests by default
        (it as HasUnitTestBuilder).enableUnitTest = false

        // disable android tests by default
        it.androidTest.enable = false
    }
}

/**
 * Configure the Library Component based on build variants.
 */
internal fun LibraryAndroidComponentsExtension.configureAndroidLibraryVariants() {
    beforeVariants {
        // disable unit tests by default
        (it as HasUnitTestBuilder).enableUnitTest = false

        // disable android tests by default
        it.androidTest.enable = false

        // only enable release build variant for the Android library project
        it.enable = it.buildType == "release"
    }
}

/**
 * Enable Compose and configure Compose options
 */
internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = the<LibrariesForLibs>().versions.androidx.compose.compiler.get()
    }

    tasks.withType<KotlinCompile>().configureEach {
        with(compilerOptions.freeCompilerArgs) {
            // strong skipping mode
            addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true"
            )

            // stability configuration
            val stabilityConfigFile = layout.projectDirectory.file("compose_stability_config.conf").asFile
            if (stabilityConfigFile.exists()) {
                addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${stabilityConfigFile.absolutePath}"
                )
            }

            // compiler metrics and reports
            if (providers.gradleProperty("composeCompilerReports").orNull == "true") {
                val composeMetricsDir = layout.buildDirectory.dir("compose_metrics").get()
                addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$composeMetricsDir",
                )
                addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$composeMetricsDir",
                )
            }
        }
    }
}
