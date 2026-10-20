kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.markdown") {
        compose()
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":core:designsystem"))
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.tracing)
            implementation(libs.jetbrains.markdown)
        }
    }
}
