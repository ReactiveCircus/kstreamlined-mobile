package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import com.android.build.api.dsl.Packaging
import com.android.build.api.dsl.TestExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File

/**
 * Apply baseline configurations on an Android Application project.
 */
internal fun ApplicationExtension.configureAndroidApplicationExtension(project: Project) {
    defaultConfig {
        targetSdk = AndroidSdk.TargetSdk
    }
    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += "en"
    }
    configureCommonAndroidExtension(project)
}

/**
 * Apply baseline configurations on an Android Library project.
 */
internal fun LibraryExtension.configureAndroidLibraryExtension(
    project: Project,
    namespace: String,
    enableAndroidResources: Boolean,
) {
    this.namespace = namespace
    this.androidResources.enable = enableAndroidResources
    configureCommonAndroidExtension(project)
}

/**
 * Apply baseline configurations on an Android Test project.
 */
internal fun TestExtension.configureAndroidTestExtension(project: Project) {
    defaultConfig {
        targetSdk = AndroidSdk.TargetSdk
    }
    configureCommonAndroidExtension(project)
}

/**
 * Apply baseline configurations on an KMP Android Library project.
 */
internal fun KotlinMultiplatformAndroidLibraryExtension.configureKmpAndroidLibraryExtension(
    project: Project,
    namespace: String? = null,
    hostTestsEnabled: Boolean = false,
) {
    compileSdk = AndroidSdk.CompileSdk
    minSdk = AndroidSdk.MinSdk
    buildToolsVersion = AndroidSdk.BuildTools

    if (namespace != null) {
        this.namespace = namespace
    }

    if (hostTestsEnabled) {
        withHostTest {}
    }

    withDeviceTest {
        animationsDisabled = true
    }

    lint {
        configureLintOptions(project)
    }

    packaging {
        configurePackagingOptions()
    }
}

/**
 * Configure the Library Component based on build variants.
 */
internal fun LibraryAndroidComponentsExtension.configureAndroidLibraryVariants() {
    beforeVariants {
        // only enable release build variant for the Android library project
        // note that this will also disable unit tests and Android tests by default
        it.enable = it.buildType == "release"
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

private fun CommonExtension.configureCommonAndroidExtension(project: Project) {
    compileSdk = AndroidSdk.CompileSdk
    buildToolsVersion = AndroidSdk.BuildTools

    defaultConfig.minSdk = AndroidSdk.MinSdk

    testOptions.animationsDisabled = true

    with(compileOptions) {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint.configureLintOptions(project)

    packaging.configurePackagingOptions()
}

private fun Lint.configureLintOptions(project: Project) {
    quiet = false
    ignoreWarnings = false
    htmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.html")
    xmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.xml")
    ignoreTestSources = true
}

private fun Packaging.configurePackagingOptions() {
    resources {
        pickFirsts.addAll(
            listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
            ),
        )
    }
}

private object AndroidSdk {
    const val MinSdk = 26
    const val TargetSdk = 36
    const val CompileSdk = 36
    const val BuildTools = "36.0.0"
}
