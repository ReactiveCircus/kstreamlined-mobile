plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue"
    buildFeatures {
        androidResources = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:data"))
    implementation(project(":kmp:presentation:kotlin-weekly-issue"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}