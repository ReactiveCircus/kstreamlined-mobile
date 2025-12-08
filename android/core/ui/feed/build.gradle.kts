plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.feed") {
        compose()
        screenshotTests()

        dependencies {
            implementation(project(":core:designsystem"))
            implementation(project(":core:ui:util"))
            implementation(project(":kmp:feed-model"))

            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.tracing)
            implementation(libs.coil.compose)
        }
    }
}
