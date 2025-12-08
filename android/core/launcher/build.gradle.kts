plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.launcher") {
        dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.browser)
            implementation(libs.kermit)
        }
    }
}
