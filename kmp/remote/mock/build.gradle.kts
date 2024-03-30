plugins {
    id("kstreamlined.kmp.common")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:remote:common"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
