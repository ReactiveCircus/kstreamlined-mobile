plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "io.github.reactivecircus.kstreamlined.android.ui.common"
    buildFeatures {
        androidResources = true
    }
}

androidComponents {
    beforeVariants {
        it.enableUnitTest = false
        it.enableAndroidTest = false
    }
}

dependencies {
    // TODO add project dependencies

    // AndroidX
    api(libs.androidx.core)

    // Compose
    api(libs.androidx.compose.ui)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.toolingPreview)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.navigation.compose)

    // Hilt AndroidX
    api(libs.androidx.hilt.navigationCompose)

    // Image loading
    api(libs.coil)

    // Logging
    api(libs.kermit)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
}
