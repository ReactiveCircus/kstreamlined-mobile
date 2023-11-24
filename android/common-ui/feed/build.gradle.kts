plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.common.ui.feed"
    buildFeatures {
        androidResources = true
    }
}

androidComponents {
    beforeVariants {
        @Suppress("UnstableApiUsage")
        it.androidTest.enable = false
    }
}

dependencies {
    implementation(project(":designsystem"))
    implementation(project(":kmp:model"))

    // Compose
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
}
