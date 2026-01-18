plugins {
    id("kstreamlined")
}

kstreamlined {
    jvmLibrary {
        dependencies {
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.compose.foundation)
        }
    }
}
