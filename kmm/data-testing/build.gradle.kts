plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("kstreamlined.kmm.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmm:data-common"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
