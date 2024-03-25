plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kmp:presentation:common"))
                implementation(project(":kmp:feed-datasource"))
                implementation(project(":kmp:feed-sync:common"))
                implementation(project(":kmp:pretty-time"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:feed-sync:testing"))
                implementation(project(":kmp:networking:testing"))
                implementation(project(":kmp:database-testing"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}
