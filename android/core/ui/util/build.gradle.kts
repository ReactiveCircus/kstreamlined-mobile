plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.ui.util") {
        compose()

        dependencies {
            implementation(libs.androidx.compose.foundation)
        }
    }
}
