plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester"
}

dependencies {
    api(project(":foundation:screenshot-testing:paparazzi"))
    implementation(project(":foundation:designsystem"))

    // AndroidX
    implementation(libs.androidx.compose.foundation)

    // Paparazzi
    implementation(libs.paparazzi)
}
