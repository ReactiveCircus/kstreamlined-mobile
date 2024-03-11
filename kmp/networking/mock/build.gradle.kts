plugins {
    id("kstreamlined.kmp.common")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:networking:common"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
