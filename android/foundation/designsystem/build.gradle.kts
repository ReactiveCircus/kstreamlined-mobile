plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.designsystem"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
}
