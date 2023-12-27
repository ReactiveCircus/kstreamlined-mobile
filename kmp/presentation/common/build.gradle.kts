plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Coroutines
                api(libs.kotlinx.coroutines.core)
            }
        }
    }
}
