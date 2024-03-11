plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kmp:networking:common"))
                implementation(project(":kmp:database"))
                api(project(":kmp:model"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:networking:testing"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
