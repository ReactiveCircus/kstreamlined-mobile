plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        create("KStreamlinedDatabase") {
            packageName.set("io.github.reactivecircus.kstreamlined.kmp.persistence.database")
        }
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.dataStore)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
