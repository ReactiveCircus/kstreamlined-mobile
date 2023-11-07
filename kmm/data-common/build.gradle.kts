plugins {
    id("kstreamlined.kmm.jvm-and-ios")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmm:apollo-models"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
