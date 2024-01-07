plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.composeutils"
}

androidComponents {
    beforeVariants {
        @Suppress("UnstableApiUsage")
        it.androidTest.enable = false
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.compose.foundation)
}
