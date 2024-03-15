plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:feed-sync:common"))
                implementation(project(":kmp:networking:common"))
                implementation(project(":kmp:database"))
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:networking:testing"))
                implementation(project(":kmp:database-testing"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
