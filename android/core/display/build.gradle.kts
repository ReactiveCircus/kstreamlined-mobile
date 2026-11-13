kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.display") {
        compose()

        dependencies {
            implementation(libs.androidx.compose.runtime)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core)
        }
    }
}
