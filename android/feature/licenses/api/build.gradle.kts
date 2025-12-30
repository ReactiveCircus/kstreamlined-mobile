plugins {
    id("kstreamlined")
}

kstreamlined {
    jvmLibrary {
        serialization()

        dependencies {
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
