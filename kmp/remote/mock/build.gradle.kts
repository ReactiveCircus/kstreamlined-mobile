plugins {
    id("kstreamlined.kmp.jvm-and-ios")
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
