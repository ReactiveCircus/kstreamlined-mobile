plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.settings"
    androidResources.enable = true
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":kmp:settings-datasource"))
    implementation(project(":kmp:presentation:settings"))
    implementation(project(":kmp:app-info"))

    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
