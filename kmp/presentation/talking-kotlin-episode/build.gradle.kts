plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("kstreamlined.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:presentation:common"))
                implementation(project(":kmp:feed-datasource"))
                implementation(project(":kmp:pretty-time"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:remote:testing"))
                implementation(project(":kmp:database-testing"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}
