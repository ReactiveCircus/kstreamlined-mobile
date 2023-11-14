plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:feed-datasource:common"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
