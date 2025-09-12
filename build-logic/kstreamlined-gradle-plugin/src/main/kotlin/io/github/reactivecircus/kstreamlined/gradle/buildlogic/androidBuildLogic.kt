package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
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
 * Apply baseline configurations on an Android Application or Library project.
 */
internal fun CommonExtension.configureCommonAndroidExtension(project: Project) {
    compileSdk = AndroidSdk.compileSdk
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig.minSdk = AndroidSdk.minSdk

    testOptions.animationsDisabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        configureLintOptions(project)
    }

    packaging {
        configurePackagingOptions()
    }
}

/**
 * Apply baseline configurations on an KMP Android Library project.
 */
internal fun KotlinMultiplatformAndroidLibraryExtension.configureKmpAndroidLibraryExtension(project: Project) {
    compileSdk = AndroidSdk.compileSdk
    minSdk = AndroidSdk.minSdk
    buildToolsVersion = AndroidSdk.buildTools

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
 * Apply baseline configurations on an Android Application project.
 */
internal fun ApplicationExtension.configureAndroidApplicationExtension() {
    defaultConfig {
        targetSdk = AndroidSdk.targetSdk
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += "en"
    }
}

/**
 * Apply baseline configurations on an Android Test project.
 */
internal fun TestExtension.configureAndroidTestExtension() {
    compileSdk = AndroidSdk.compileSdk
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
            )
        )
    }
}
