import java.time.Instant

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidApp(
        namespace = "io.github.reactivecircus.kstreamlined.android",
        applicationId = "io.github.reactivecircus.kstreamlined",
        baseApkName = "kstreamlined",
    ) {
        buildConfigs { variant ->
            if (variant.buildType == "debug") {
                // disable LeakCanary for CI builds
                buildConfigField(key = "ENABLE_LEAK_CANARY", value = !isCiBuild)
            }
            when (variant.flavorName) {
                ProductFlavors.Prod -> {
                    buildConfigField(key = "ENABLE_ANALYTICS", value = googleServicesJsonExists)
                    buildConfigField(key = "ENABLE_CRASH_REPORTING", value = googleServicesJsonExists)
                    buildConfigField(key = "API_ENDPOINT", value = envOrProp("KSTREAMLINED_API_ENDPOINT"))
                }
                ProductFlavors.Dev -> {
                    buildConfigField(key = "ENABLE_ANALYTICS", value = googleServicesJsonExists)
                    buildConfigField(key = "ENABLE_CRASH_REPORTING", value = googleServicesJsonExists)
                    buildConfigField(key = "API_ENDPOINT", value = envOrProp("KSTREAMLINED_API_ENDPOINT"))
                }
                ProductFlavors.Demo -> {
                    buildConfigField(key = "ENABLE_ANALYTICS", value = false)
                    buildConfigField(key = "ENABLE_CRASH_REPORTING", value = false)
                }
                ProductFlavors.Mock -> {
                    buildConfigField(key = "ENABLE_ANALYTICS", value = false)
                    buildConfigField(key = "ENABLE_CRASH_REPORTING", value = false)
                }
            }
            buildConfigField(key = "NETWORK_TIMEOUT_SECONDS", value = 10)
            buildConfigField(key = "SOURCE_CODE_URL", value = "https://github.com/ReactiveCircus/kstreamlined-mobile")
        }
        resValues { variant ->
            if (variant.buildType == "debug") {
                // hide LeakCanary icon for CI builds
                resValueFromBoolean(key = "leak_canary_add_launcher_icon", value = !isCiBuild)
                // override app name for LeakCanary
                resValueFromString(key = "leak_canary_display_activity_label", value = "KStreamlined leaks")
                // disable automatic watcher install for LeakCanary
                resValueFromBoolean(key = "leak_canary_watcher_auto_install", value = false)
                // concatenate build variant to app name
                resValueFromString(key = "app_name", value = "KStreamlined-${variant.name}")
            } else {
                // set app_name for release build
                resValueFromString(key = "app_name", value = "KStreamlined")
            }
        }
        signing {
            debug {
                storeFile(rootDir.resolve("android/secrets/debug.keystore"))
                storePassword("kstreamlined-debug")
                keyAlias("kstreamlined-debug")
                keyPassword("kstreamlined-debug")
            }
            release {
                storeFile(rootDir.resolve("android/secrets/kstreamlined.jks"))
                storePassword(envOrProp("KSTREAMLINED_STORE_PASSWORD").get())
                keyAlias(envOrProp("KSTREAMLINED_KEY_ALIAS").get())
                keyPassword(envOrProp("KSTREAMLINED_KEY_PASSWORD").get())
            }
        }
        keepRule(file("keep-rules.pro"))
        versioning {
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
        playPublishing(rootDir.resolve("android/secrets/play-publishing.json"))
        firebasePerf()
        firebaseCrashlytics()
        firebaseAppDistribution(
            testerGroups = "kstreamlined-qa",
            serviceAccountCredentials = rootDir.resolve("secrets/firebase-key.json"),
        )
        consumeBaselineProfile(":benchmark")
        generateLicensesInfo()
        compose()
        hilt()
        serialization()

        dependencies {
            implementation(project(":kmp:app-info"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:feed-sync:runtime"))
            implementation(project(":kmp:settings-datasource"))
            implementation(project(":kmp:database"))
            implementation(project(":kmp:network-monitor:runtime"))
            mockImplementation(project(":kmp:remote:mock"))
            demoImplementation(project(":kmp:remote:edge"))
            devImplementation(project(":kmp:remote:cloud"))
            prodImplementation(project(":kmp:remote:cloud"))
            implementation(project(":core:designsystem"))
            implementation(project(":feature:content-viewer"))
            implementation(project(":feature:home"))
            implementation(project(":feature:kotlin-weekly-issue"))
            implementation(project(":feature:licenses"))
            implementation(project(":feature:saved-for-later"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:talking-kotlin-episode"))
            implementation(project(":core:scheduled-work"))

            releaseImplementation(libs.firebase.perf)
            implementation(libs.firebase.crashlytics)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.navigation3)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.coreSplashscreen)
            implementation(libs.androidx.profileinstaller)
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.runtime.tracing)
            implementation(libs.androidx.work.runtime)
            implementation(libs.androidx.tracing)
            implementation(libs.okhttp)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.sqldelight.androidDriver)
            debugImplementation(libs.leakcanary.android)
            implementation(libs.leakcanary.plumber)
        }
    }
}
