plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed"
}

androidComponents {
    beforeVariants {
        @Suppress("UnstableApiUsage")
        it.androidTest.enable = false
    }
}

dependencies {
    implementation(project(":foundation:designsystem"))
    implementation(project(":foundation:compose-utils"))
    implementation(project(":kmp:model"))

    // AndroidX
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)

    // Image loading
    implementation(libs.coil.compose)
}
