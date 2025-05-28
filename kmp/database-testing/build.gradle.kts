plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
    }
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
