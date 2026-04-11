plugins {
    id("kstreamlined")
    id("io.github.reactivecircus.cocoon")
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

cocoon {
    annotation.set("io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined")
    wrappingFunction.set("io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.KSThemeWithSurface")
}
