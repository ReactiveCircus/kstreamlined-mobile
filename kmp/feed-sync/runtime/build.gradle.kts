plugins {
    id("kstreamlined.kmp.jvm-and-ios")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:feed-sync:common"))
                implementation(project(":kmp:remote:common"))
                implementation(project(":kmp:database"))
                implementation(project(":kmp:network-monitor:common"))
                implementation(libs.kermit)
                implementation(libs.ksoup)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:remote:testing"))
                implementation(project(":kmp:database-testing"))
                implementation(project(":kmp:network-monitor:testing"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}
