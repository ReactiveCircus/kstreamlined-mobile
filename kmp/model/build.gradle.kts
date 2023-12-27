plugins {
    id("kstreamlined.kmp.common")
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
