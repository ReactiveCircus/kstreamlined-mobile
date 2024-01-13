plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.contentviewer"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:data"))
    implementation(project(":kmp:presentation:content-viewer"))

    // AndroidX
    implementation(libs.androidx.activity.compose)
}