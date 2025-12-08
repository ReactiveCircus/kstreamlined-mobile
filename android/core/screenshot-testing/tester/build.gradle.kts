plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester") {
        compose()

        dependencies {
            api(project(":core:screenshot-testing:paparazzi"))
            implementation(project(":core:designsystem"))

            implementation(libs.androidx.compose.foundation)
            implementation(libs.paparazzi)
        }
    }
}
