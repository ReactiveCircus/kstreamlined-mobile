plugins {
    id("kstreamlined")
    id("kstreamlined.compose")
}

kstreamlined {
    jvmLibrary {
        mainDependencies {
            implementation(libs.androidx.compose.runtime)
            implementation(libs.paparazzi)
        }
    }
}
