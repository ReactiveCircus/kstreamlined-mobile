plugins {
    id("kstreamlined.android.library")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.common"
    androidResources.enable = true
}

dependencies {
    api(project(":foundation:designsystem"))

    api(libs.androidx.core)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewmodelCompose)
    api(libs.androidx.hilt.navigationCompose)
    implementation(libs.androidx.browser)
    api(libs.coil.compose)
    api(libs.kermit)
    api(libs.kotlinx.coroutines.core)
}
