package io.github.reactivecircus.kstreamlined.gradle.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File

/**
 * Apply baseline configurations on an Application or Library project.
 */
internal fun CommonExtension<*, *, *, *, *, *>.configureCommonAndroidExtension(project: Project) {
    compileSdk = AndroidSdk.compileSdk
    buildToolsVersion = AndroidSdk.buildTools

    defaultConfig {
        minSdk = AndroidSdk.minSdk
    }

    testOptions.animationsDisabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        quiet = false
        ignoreWarnings = false
        htmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.html")
        xmlOutput = File("${project.layout.buildDirectory.get()}/reports/lint/lint-reports.xml")
        ignoreTestSources = true
    }

    packaging {
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
    setCompileSdkVersion(AndroidSdk.compileSdk)
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
        // only enable release build variant for the Android library project
        it.enable = it.buildType == "release"

        // disable unit tests by default
        (it as HasUnitTestBuilder).enableUnitTest = false

        // disable android tests by default
        it.androidTest.enable = false
    }
}
