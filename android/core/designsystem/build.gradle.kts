plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.designsystem") {
        compose()
        androidResources()
        generatePaintersFromDrawables(
            containerName = "KSIcons",
            drawablePrefix = "ic_",
            subpackage = "foundation.icon",
        )
        screenshotTests()

        dependencies {
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
        }
    }
}
