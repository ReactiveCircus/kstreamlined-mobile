plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.compose")
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
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:kotlin-weekly-issue"))

    // AndroidX
    implementation(libs.androidx.tracing)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
