kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.feed") {
        compose()
        androidResources()
        screenshotTests()

        dependencies {
            api(project(":kmp:feed-model"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:ui:util"))

            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.tracing)
            implementation(libs.coil.compose)
        }
    }
}
