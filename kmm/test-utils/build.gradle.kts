plugins {
    id("kstreamlined.kmm.jvm-and-ios")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.test)
            }
        }
    }
}
