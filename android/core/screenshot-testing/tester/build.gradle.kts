plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester"
}

dependencies {
    api(project(":core:screenshot-testing:paparazzi"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.compose.foundation)
    implementation(libs.paparazzi)
}
