plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
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

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
