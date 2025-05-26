plugins {
    id("kstreamlined.kmp.jvm-and-ios")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:database"))
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.sqldelight.sqliteDriver)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.sqldelight.nativeDriver)
            }
        }
    }
}
