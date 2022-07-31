plugins {
    id("kstreamlined.kmm.jvm-and-ios")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":kmm:apollo-models"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
