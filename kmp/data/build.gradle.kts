plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kmp:feed-datasource:common"))
                implementation(project(":kmp:persistence"))
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
