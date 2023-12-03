plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
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
