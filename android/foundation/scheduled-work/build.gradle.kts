plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork"
}

dependencies {
    implementation(project(":kmp:feed-sync:common"))

    // AndroidX
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
