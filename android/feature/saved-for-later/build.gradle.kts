plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.savedforlater"
    androidResources.enable = true
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":core:common-ui:feed"))
    implementation(project(":kmp:feed-datasource"))
    implementation(project(":kmp:presentation:saved-for-later"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
