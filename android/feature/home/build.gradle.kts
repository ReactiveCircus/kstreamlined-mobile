plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.home"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":foundation:common-ui:feed"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:feed-sync:common"))
    implementation(project(":kmp:presentation:home"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
