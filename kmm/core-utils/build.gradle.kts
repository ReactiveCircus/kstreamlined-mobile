plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("kstreamlined.kmm.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
