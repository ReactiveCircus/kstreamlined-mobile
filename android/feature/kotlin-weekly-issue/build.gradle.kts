plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue"
    androidResources.enable = true
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:kotlin-weekly-issue"))

    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
