plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.common"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    api(project(":foundation:designsystem"))

    // AndroidX
    api(libs.androidx.core)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.browser)

    // Image loading
    api(libs.coil.compose)

    // Logging
    api(libs.kermit)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
}
