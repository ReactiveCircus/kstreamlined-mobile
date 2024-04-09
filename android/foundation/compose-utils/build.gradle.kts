plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.composeutils"
}

dependencies {
    // AndroidX
    implementation(libs.androidx.compose.foundation)
}
