plugins {
    id("kstreamlined.kmm.jvm-and-ios")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.test)
            }
        }
    }
}
