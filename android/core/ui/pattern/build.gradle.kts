plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.pattern") {
        compose()
        androidResources()
        screenshotTests()

        dependencies {
            implementation(project(":core:designsystem"))
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.tracing)
            implementation(libs.coil.compose)
        }
    }
}
