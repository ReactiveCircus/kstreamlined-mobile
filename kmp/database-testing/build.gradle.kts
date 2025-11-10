plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }

        commonMainDependencies {
            api(project(":kmp:database"))
        }
        jvmMainDependencies {
            implementation(libs.sqldelight.sqliteDriver)
        }
        iosMainDependencies {
            implementation(libs.sqldelight.nativeDriver)
        }
    }
}
