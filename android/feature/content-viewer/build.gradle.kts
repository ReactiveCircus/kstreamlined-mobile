plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.contentviewer"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:content-viewer"))

    // AndroidX
    implementation(libs.androidx.activity.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
