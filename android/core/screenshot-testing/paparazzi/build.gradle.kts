plugins {
    id("kstreamlined")
}

kstreamlined {
    jvmLibrary {
        compose()

        dependencies {
            implementation(libs.androidx.compose.runtime)
            implementation(libs.paparazzi)
        }
    }
}
