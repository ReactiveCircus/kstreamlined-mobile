@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.buildlogic

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File

/**
 * Apply baseline configurations on an Application or Library project.
 */
internal fun TestedExtension.configureCommonAndroidExtension() {
    setCompileSdkVersion(AndroidSdk.compileSdk)
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
    }

    testOptions.animationsDisabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
 * Apply baseline configurations on an Application project.
 */
internal fun BaseAppModuleExtension.configureAndroidApplicationExtension(project: Project) {
    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += "en"
    }

    lint {
        quiet = false
        ignoreWarnings = false
        htmlReport = true
        xmlReport = true
        htmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.html")
        xmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.xml")
        ignoreTestSources = true
    }
}

/**
 * Apply baseline configurations on an Android Test project.
 */
internal fun TestExtension.configureAndroidTestExtension() {
    setCompileSdkVersion(AndroidSdk.compileSdk)
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
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
