plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.contentviewer"
    androidResources.enable = true
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:content-viewer"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
