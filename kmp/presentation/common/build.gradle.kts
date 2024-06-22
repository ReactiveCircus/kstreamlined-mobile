plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("kstreamlined.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Coroutines
                api(libs.kotlinx.coroutines.core)
                api(libs.molecule.runtime)
            }
        }
    }
}
