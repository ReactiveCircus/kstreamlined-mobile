plugins {
    id("kstreamlined")
    id("io.github.reactivecircus.v2p")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.designsystem") {
        compose()
        androidResources()
        screenshotTests()

        dependencies {
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.compose.ui.tooling)
        }
    }
}

v2p {
    generate("KSIcons") {
        prefix = "ic_"
        generateAsListFunction = true
    }
}
