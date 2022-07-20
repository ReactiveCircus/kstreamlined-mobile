plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("com.apollographql.apollo3")
}

apollo {
    packageNamesFromFilePaths()
    codegenModels.set("responseBased")
    flattenModels.set(true)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.apollo.api)
            }
        }
    }
}
