plugins {
    id("kstreamlined.kmp.jvm-and-ios")
    id("kstreamlined.kmp.test")
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        create("KStreamlinedDatabase") {
            packageName.set("io.github.reactivecircus.kstreamlined.kmp.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.sqldelight.coroutinesExtensions)
            }
        }
    }
}
