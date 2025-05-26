plugins {
    id("kstreamlined.kmp.jvm-and-ios")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.datetime)
            }
        }
    }
}
