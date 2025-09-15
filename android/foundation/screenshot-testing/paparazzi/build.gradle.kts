plugins {
    id("kstreamlined.kotlin.jvm")
    id("kstreamlined.compose")
}

dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(libs.paparazzi)
}
