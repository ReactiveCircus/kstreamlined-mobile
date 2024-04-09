import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.firebase.perf.plugin.FirebasePerfExtension
import io.github.reactivecircus.kstreamlined.buildlogic.FlavorDimensions
import io.github.reactivecircus.kstreamlined.buildlogic.ProductFlavors
import io.github.reactivecircus.kstreamlined.buildlogic.addBuildConfigField
import io.github.reactivecircus.kstreamlined.buildlogic.addResValue
import io.github.reactivecircus.kstreamlined.buildlogic.demoImplementation
import io.github.reactivecircus.kstreamlined.buildlogic.devImplementation
import io.github.reactivecircus.kstreamlined.buildlogic.envOrProp
import io.github.reactivecircus.kstreamlined.buildlogic.isCiBuild
import io.github.reactivecircus.kstreamlined.buildlogic.mockImplementation
import io.github.reactivecircus.kstreamlined.buildlogic.prodImplementation
import java.time.Instant

plugins {
    id("kstreamlined.android.application")
    id("kstreamlined.android.compose")
    id("kstreamlined.ksp")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("io.github.reactivecircus.app-versioning")
    id("com.github.triplet.play")
    id("androidx.baselineprofile")
}

// only apply google-services plugin if google-services.json exists
val googleServicesJsonExists = fileTree("src").matching {
    include("**/google-services.json")
}.isEmpty.not()
if (googleServicesJsonExists) {
    apply(plugin = "com.google.gms.google-services")
}

// disable google services plugin for demo and mock flavors
tasks.configureEach {
    if ("process(Demo|Mock)DebugGoogleServices".toRegex().matches(name)) {
        enabled = false
    }
}

appVersioning {
    enabled.set(
        providers.environmentVariable("ENABLE_APP_VERSIONING").orElse("true").map { it.toBoolean() }
    )
    overrideVersionCode { _, _, _ ->
        Instant.now().epochSecond.toInt()
    }
    overrideVersionName { gitTag, _, _ ->
        "${gitTag.rawTagName} (${gitTag.commitHash})"
    }
}

play {
    enabled.set(false) // only enable publishing for prodRelease variant in `playConfigs` block
    serviceAccountCredentials.set(rootProject.file("android/secrets/play-publishing.json"))
    defaultToAppBundles.set(true)
}

baselineProfile {
    mergeIntoMain = true
    baselineProfileOutputDir = "baselineProfiles"
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android"
    buildFeatures {
        buildConfig = true
        resValues = true
    }

    defaultConfig {
        applicationId = "io.github.reactivecircus.kstreamlined"
        base.archivesName.set("kstreamlined")

        testApplicationId = "io.github.reactivecircus.kstreamlined.android.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        named("debug") {
            storeFile = rootProject.file("android/secrets/debug.keystore")
            storePassword = "kstreamlined-debug"
            keyAlias = "kstreamlined-debug"
            keyPassword = "kstreamlined-debug"
        }
        register("release") {
            storeFile = rootProject.file("android/secrets/kstreamlined.jks")
            storePassword = envOrProp("KSTREAMLINED_STORE_PASSWORD").get()
            keyAlias = envOrProp("KSTREAMLINED_KEY_ALIAS").get()
            keyPassword = envOrProp("KSTREAMLINED_KEY_PASSWORD").get()
        }
    }

    buildTypes {
        debug {
            isDefault = true
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")

            // disable performance monitoring plugin for debug builds
            (this as ExtensionAware).extensions.configure<FirebasePerfExtension> {
                setInstrumentationEnabled(false)
            }
        }
        release {
            matchingFallbacks.add("release")
            if (rootProject.file("android/secrets/kstreamlined.jks").exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "shrinker-rules.pro")

            // only upload mapping file on CI
            (this as ExtensionAware).extensions.configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = isCiBuild
            }
        }
    }

    flavorDimensions.add(FlavorDimensions.Environment)
    productFlavors {
        register(ProductFlavors.Mock) {
            dimension = FlavorDimensions.Environment
            applicationIdSuffix = ".${ProductFlavors.Mock}"
        }
        register(ProductFlavors.Dev) {
            isDefault = true
            dimension = FlavorDimensions.Environment
            applicationIdSuffix = ".${ProductFlavors.Dev}"

            // distribute dev flavor for QA
            firebaseAppDistribution {
                groups = "kstreamlined-qa"
                serviceCredentialsFile = rootProject.file("secrets/firebase-key.json").absolutePath
            }
        }
        register(ProductFlavors.Demo) {
            dimension = FlavorDimensions.Environment
            applicationIdSuffix = ".${ProductFlavors.Demo}"
        }
        register(ProductFlavors.Prod) {
            dimension = FlavorDimensions.Environment
        }
    }

    sourceSets {
        // common source set for dev and prod
        named(ProductFlavors.Dev) {
            java.srcDir("src/devAndProd/java")
        }
        named(ProductFlavors.Prod) {
            java.srcDir("src/devAndProd/java")
        }
    }

    playConfigs {
        register(ProductFlavors.Prod) {
            enabled.set(true)
        }
    }
}

androidComponents {
    beforeVariants {
        it.enable = it.name in listOf(
            "devDebug",
            "demoDebug",
            "mockDebug",
            "prodRelease",
            "devNonMinifiedRelease",
            "devBenchmarkRelease",
            "prodBenchmarkRelease",
        )
    }

    onVariants {
        if (it.buildType == "debug") {
            // disable LeakCanary for CI builds
            it.addBuildConfigField(key = "ENABLE_LEAK_CANARY", value = !isCiBuild)

            // hide LeakCanary icon for CI builds
            it.addResValue(key = "leak_canary_add_launcher_icon", type = "bool", value = "${!isCiBuild}")

            // override app name for LeakCanary
            it.addResValue(key = "leak_canary_display_activity_label", type = "string", value = "KStreamlined leaks")

            // disable automatic watcher install for LeakCanary
            it.addResValue(key = "leak_canary_watcher_auto_install", type = "bool", value = "false")

            // concatenate build variant to app name
            it.addResValue(key = "app_name", type = "string", value = "KStreamlined-${it.name}")
        } else {
            // set app_name for release build
            it.addResValue(key = "app_name", type = "string", value = "KStreamlined")
        }

        when (it.flavorName) {
            ProductFlavors.Prod -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = googleServicesJsonExists)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = googleServicesJsonExists)
                it.addBuildConfigField(key = "API_ENDPOINT", value = "\"${envOrProp("KSTREAMLINED_API_ENDPOINT").get()}\"")
            }
            ProductFlavors.Dev -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = googleServicesJsonExists)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = googleServicesJsonExists)
                it.addBuildConfigField(key = "API_ENDPOINT", value = "\"${envOrProp("KSTREAMLINED_API_ENDPOINT").get()}\"")
            }
            ProductFlavors.Demo -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = false)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = false)
            }
            ProductFlavors.Mock -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = false)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = false)
            }
        }
        it.addBuildConfigField(key = "NETWORK_TIMEOUT_SECONDS", value = 10)
    }
}

dependencies {
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:feed-sync:runtime"))
    implementation(project(":kmp:database"))
    implementation(project(":kmp:network-monitor:runtime"))
    mockImplementation(project(":kmp:remote:mock"))
    demoImplementation(project(":kmp:remote:edge"))
    devImplementation(project(":kmp:remote:cloud"))
    prodImplementation(project(":kmp:remote:cloud"))

    implementation(project(":feature:common"))
    implementation(project(":feature:content-viewer"))
    implementation(project(":feature:home"))
    implementation(project(":feature:kotlin-weekly-issue"))
    implementation(project(":feature:saved-for-later"))
    implementation(project(":feature:talking-kotlin-episode"))

    implementation(project(":foundation:scheduled-work"))

    baselineProfile(project(":benchmark"))

    // Firebase
    releaseImplementation(libs.firebase.perf)
    implementation(libs.firebase.crashlytics)

    // AndroidX
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.coreSplashscreen)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.tracing)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // OkHttp
    implementation(libs.okhttp)

    // Image loading
    implementation(libs.coil.svg)
    implementation(libs.coil.network)

    // SQLDelight
    implementation(libs.sqldelight.androidDriver)

    // LeakCanary
    debugImplementation(libs.leakcanary.android)
    implementation(libs.leakcanary.plumber)
}
