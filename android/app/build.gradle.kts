import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.firebase.perf.plugin.FirebasePerfExtension
import io.github.reactivecircus.kstreamlined.buildlogic.FlavorDimensions
import io.github.reactivecircus.kstreamlined.buildlogic.ProductFlavors
import io.github.reactivecircus.kstreamlined.buildlogic.addBuildConfigField
import io.github.reactivecircus.kstreamlined.buildlogic.addResValue
import io.github.reactivecircus.kstreamlined.buildlogic.benchmarkImplementation
import io.github.reactivecircus.kstreamlined.buildlogic.envOrProp
import io.github.reactivecircus.kstreamlined.buildlogic.isCiBuild

plugins {
    id("kstreamlined.android.application")
    id("kstreamlined.android.application.compose")
    id("kstreamlined.kapt")
    id("com.google.dagger.hilt.android")
    // TODO id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("com.github.triplet.play")
}

play {
    enabled.set(false) // only enable publishing for prodRelease variant in `playConfigs` block
    serviceAccountCredentials.set(rootProject.file("android/secrets/play-publishing.json"))
    defaultToAppBundles.set(true)
}

@Suppress("UnstableApiUsage")
android {
    namespace = "io.github.reactivecircus.kstreamlined.android"
    buildFeatures {
        buildConfig = true
        resValues = true
    }

    defaultConfig {
        // TODO setup app-versioning
        versionCode = 1
        versionName = "1.0.0"

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
            storePassword = envOrProp("KSTREAMLINED_STORE_PASSWORD")
            keyAlias = envOrProp("KSTREAMLINED_KEY_ALIAS")
            keyPassword = envOrProp("KSTREAMLINED_KEY_PASSWORD")
        }
    }

    buildTypes {
        val debug by getting {
            signingConfig = signingConfigs.getByName("debug")

            // disable performance monitoring plugin for debug builds
            (this as ExtensionAware).extensions.configure<FirebasePerfExtension> {
                setInstrumentationEnabled(false)
            }
        }
        val release by getting {
            if (rootProject.file("android/secrets/kstreamlined.jks").exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "shrinker-rules.pro")

            // only upload mapping file on CI
            (this as ExtensionAware).extensions.configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = isCiBuild
            }
        }
        val benchmark by creating {
            initWith(release)
            matchingFallbacks.add("release")
            proguardFiles("benchmark-rules.pro")
            signingConfig = signingConfigs.getByName("debug")

            // do not upload mapping file for benchmark builds
            (this as ExtensionAware).extensions.configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = isCiBuild
            }
        }
    }

    flavorDimensions.add(FlavorDimensions.ENVIRONMENT)

    productFlavors {
        register(ProductFlavors.MOCK) {
            applicationIdSuffix = ".${ProductFlavors.MOCK}"
        }
        register(ProductFlavors.DEV) {
            applicationIdSuffix = ".${ProductFlavors.DEV}"

            // distribute dev flavor for QA
            firebaseAppDistribution {
                groups = "kstreamlined-qa"
                serviceCredentialsFile = rootProject.file("secrets/firebase-key.json").absolutePath
            }
        }
        register(ProductFlavors.PROD) {}
    }

    sourceSets {
        // common source set for dev and prod
        named(ProductFlavors.DEV) {
            java.srcDir("src/online/java")
        }
        named(ProductFlavors.PROD) {
            java.srcDir("src/online/java")
        }
    }

    playConfigs {
        register(ProductFlavors.PROD) {
            enabled.set(true)
        }
    }
}

// disable google services plugin for mock flavor
tasks.whenTaskAdded {
    if (name == "processMockDebugGoogleServices") {
        enabled = false
    }
}

androidComponents {
    // disable mockRelease, devRelease, prodDebug, mockBenchmark, prodBenchmark build variants
    beforeVariants {
        it.enable = it.flavorName == ProductFlavors.PROD && it.buildType == "release"
            || it.flavorName != ProductFlavors.PROD && it.buildType == "debug"
            || it.flavorName == ProductFlavors.DEV && it.buildType == "benchmark"
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

            // concatenate build type to app name for benchmark builds
            if (it.buildType == "benchmark") {
                it.addResValue(key = "app_name", type = "string", value = "KStreamlined-${it.buildType}")
            }
        }

        when (it.flavorName) {
            ProductFlavors.PROD -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = true)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = true)
                it.addBuildConfigField(key = "API_ENDPOINT", value = "\"https://kstreamlined/graphql\"")
            }
            ProductFlavors.DEV -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = true)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = true)
                it.addBuildConfigField(key = "API_ENDPOINT", value = "\"https://kstreamlined/graphql\"")
            }
            ProductFlavors.MOCK -> {
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = false)
                it.addBuildConfigField(key = "ENABLE_CRASH_REPORTING", value = false)
            }
        }
        it.addBuildConfigField(key = "NETWORK_TIMEOUT_SECONDS", value = 10)
    }
}

dependencies {
    // Firebase
    releaseImplementation(libs.firebase.perf)
    benchmarkImplementation(libs.firebase.perf)
    implementation(libs.firebase.crashlytics)

    // AndroidX
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.coreSplashscreen)
    implementation(libs.androidx.profileinstaller)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // TODO remove
    // Image loading
    implementation(libs.coil)

    // LeakCanary
    debugImplementation(libs.leakcanary.android)
    implementation(libs.leakcanary.plumber)
}
