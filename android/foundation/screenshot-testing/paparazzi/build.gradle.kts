plugins {
    id("kstreamlined.kotlin.jvm")
    id("kstreamlined.compose")
}

dependencies {
    // AndroidX
    implementation(libs.androidx.compose.runtime)

    // Paparazzi
    implementation(libs.paparazzi)
}
