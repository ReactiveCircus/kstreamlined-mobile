plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.savedforlater"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":common-ui:feed"))
    implementation(project(":kmp:data"))
    implementation(project(":kmp:presentation:saved-for-later"))
}
