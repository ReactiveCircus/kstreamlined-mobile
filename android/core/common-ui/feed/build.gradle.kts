plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.core.commonui.feed"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:compose-utils"))
    implementation(project(":kmp:feed-model"))

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.tracing)
    implementation(libs.coil.compose)
}
