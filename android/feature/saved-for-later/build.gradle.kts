plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.savedforlater"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":foundation:common-ui:feed"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:saved-for-later"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
