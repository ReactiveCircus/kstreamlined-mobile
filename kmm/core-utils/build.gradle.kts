plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("kstreamlined.kmm.test")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
