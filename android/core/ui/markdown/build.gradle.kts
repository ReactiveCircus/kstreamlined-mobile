kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.markdown") {
        unitTests()

        dependencies {
            implementation(libs.jetbrains.markdown)
        }
    }
}
