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

    implementation(libs.androidx.compose.foundation)
    implementation(libs.paparazzi)
}
